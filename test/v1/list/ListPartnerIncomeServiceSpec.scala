package v1.list

import api.models.errors.*
import api.models.outcomes.ResponseWrapper
import api.services.{ServiceOutcome, ServiceSpec}
import v1.list.ListPartnerIncomeFixtures.*
import v1.list.model.response.ListPartnerIncomeResponse

import scala.concurrent.Future

class ListPartnerIncomeServiceSpec extends ServiceSpec {

  trait Test extends MockListPartnerIncomeConnector {
    val service = new ListPartnerIncomeService(mockConnector)
  }
        
  "listPartnerIncome" when {
    "the connector call is successful" should {
      "return a mapped result" in new Test {
        val downstreamResponse: ResponseWrapper[ListPartnerIncomeResponse] = ResponseWrapper(correlationId, responseModel)
        MockedListPartnerIncomeConnector.listPartnerIncome(request).returns(Future.successful(Right(downstreamResponse)))
        await(service.listPartnerIncome(request)) shouldBe Right(downstreamResponse)
      }
    }
    "the connector call is unsuccessful" should {
      def serviceError(downstreamErrorCode: String, error: MtdError): Unit =
        s"return ${error.code} when $downstreamErrorCode error is returned from the service" in new Test {
          MockedListPartnerIncomeConnector
            .listPartnerIncome(request)
            .returns(Future.successful(Left(ResponseWrapper(correlationId, DownstreamErrors.single(DownstreamErrorCode(downstreamErrorCode))))))

          val result: ServiceOutcome[ListPartnerIncomeResponse] = await(service.listPartnerIncome(request))
          result shouldBe Left(ErrorWrapper(correlationId, error))
        }

      val errors = List(
        "INVALID_TAXABLE_ENTITY_ID" -> NinoFormatError,
        "INVALID_TAX_YEAR"          -> TaxYearFormatError,
        "INVALID_CORRELATION_ID"    -> InternalError,
        "NO_DATA_FOUND"             -> NotFoundError,
        "TAX_YEAR_NOT_SUPPORTED"    -> RuleTaxYearNotSupportedError
      )
      
      errors.foreach(serviceError.tupled)
    }
  }

}
