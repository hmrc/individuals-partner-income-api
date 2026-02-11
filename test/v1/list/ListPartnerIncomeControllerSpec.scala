package v1.list

import api.config.MockAppConfig
import api.controllers.{ControllerBaseSpec, ControllerTestRunner}
import api.models.errors.{ErrorWrapper, NinoFormatError, TaxYearFormatError}
import api.models.outcomes.ResponseWrapper
import api.services.{MockEnrolmentsAuthService, MockMtdIdLookupService}
import api.utils.MockIdGenerator
import play.api.Configuration
import play.api.mvc.Result
import v1.list.ListPartnerIncomeFixtures.*

import scala.concurrent.{ExecutionContext, Future}

class ListPartnerIncomeControllerSpec
    extends ControllerBaseSpec
    with ControllerTestRunner
    with MockEnrolmentsAuthService
    with MockMtdIdLookupService
    with MockListPartnerIncomeService
    with MockListPartnerIncomeValidatorFactory
    with MockIdGenerator
    with MockAppConfig {

  trait Test extends ControllerTest {
    implicit val ec: ExecutionContext = scala.concurrent.ExecutionContext.global

    val controller: ListPartnerIncomeController =
      new ListPartnerIncomeController(mockEnrolmentsAuthService, mockMtdIdLookupService, cc, mockService, mockValidatorFactory, mockIdGenerator)

    MockedAppConfig.featureSwitchConfig.anyNumberOfTimes() returns Configuration(
      "supporting-agents-access-control.enabled" -> true
    )

    MockedAppConfig.endpointAllowsSupportingAgents(controller.endpointName).anyNumberOfTimes() returns false

    protected def callController(): Future[Result] = controller.listPartnerIncome(nino.nino, taxYear.asMtd)(fakeGetRequest)
  }

  "listPartnerIncome" when {
    "handling a valid request" should {
      "return a success response with status OK" in new Test {
        willUseValidator(returningSuccess(request))
        MockedListPartnerIncomeService
          .listPartnerIncome(request)
          .returns(Future.successful(Right(ResponseWrapper(correlationId, responseModel))))

        runOkTest(OK, Some(mtdJson))
      }
    }

    "the parser validation fails" should {
      "return the error per spec" in new Test {
        willUseValidator(returning(NinoFormatError))
        runErrorTest(NinoFormatError)
      }
    }

    "the service returns an error" in new Test {
      willUseValidator(returningSuccess(request))

      MockedListPartnerIncomeService
        .listPartnerIncome(request)
        .returns(Future.successful(Left(ErrorWrapper(correlationId, TaxYearFormatError))))
      
      runErrorTest(TaxYearFormatError)
    }
  }

}
