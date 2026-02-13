/*
 * Copyright 2026 HM Revenue & Customs
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package v1.retrievePartnerIncome

import api.config.MockAppConfig
import api.controllers.{ControllerBaseSpec, ControllerTestRunner}
import api.models.errors.*
import api.models.outcomes.ResponseWrapper
import api.services.MockAuditService
import play.api.Configuration
import play.api.mvc.Result
import v1.retrievePartnerIncome.RetrievePartnerIncomeFixtures.*

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future

class RetrievePartnerIncomeControllerSpec
    extends ControllerBaseSpec
    with ControllerTestRunner
    with MockAppConfig
    with MockRetrievePartnerIncomeService
    with MockRetrievePartnerIncomeValidatorFactory
    with MockAuditService {

  "retrievePartnerIncome" should {
    "return OK" when {
      "the request is valid" in new Test {
        willUseValidator(returningSuccess(request))

        MockRetrievePartnerIncomeService
          .retrieve(request)
          .returns(Future.successful(Right(ResponseWrapper(correlationId, responseModel))))

        runOkTest(expectedStatus = OK, maybeExpectedResponseBody = Some(mtdJson))
      }
    }

    "return the error as per spec" when {
      "the parser validation fails" in new Test {
        willUseValidator(returning(PartnershipUtrFormatError))
        runErrorTest(PartnershipUtrFormatError)
      }

      "the service returns an error" in new Test {
        willUseValidator(returningSuccess(request))

        MockRetrievePartnerIncomeService
          .retrieve(request)
          .returns(Future.successful(Left(ErrorWrapper(correlationId, RuleTaxYearNotSupportedError, None))))

        runErrorTest(RuleTaxYearNotSupportedError)
      }
    }
  }

  private trait Test extends ControllerTest {

    val controller: RetrievePartnerIncomeController = new RetrievePartnerIncomeController(
      authService = mockEnrolmentsAuthService,
      lookupService = mockMtdIdLookupService,
      service = mockRetrievePartnerIncomeService,
      validatorFactory = mockRetrievePartnerIncomeValidatorFactory,
      cc = cc,
      idGenerator = mockIdGenerator
    )

    protected def callController(): Future[Result] = controller.retrievePartnerIncome(validNino, taxYear, partnershipUtr)(fakeGetRequest)

    MockedAppConfig.featureSwitchConfig.anyNumberOfTimes() returns Configuration(
      "supporting-agents-access-control.enabled" -> true
    )

    MockedAppConfig.endpointAllowsSupportingAgents(controller.endpointName).anyNumberOfTimes() returns false

  }

}
