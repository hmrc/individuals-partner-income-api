package v1.createAmendPartnerIncome

import api.connectors.{ConnectorSpec, DownstreamOutcome}
import api.models.domain.{Nino, TaxYear}
import api.models.errors.{DownstreamErrorCode, DownstreamErrors}
import api.models.outcomes.ResponseWrapper
import uk.gov.hmrc.http.StringContextOps
import v1.createAmendPartnerIncome.CreateAmendPartnerIncomeFixtures.{fullDownstreamJson, fullRequestModel}
import v1.createAmendPartnerIncome.model.request.CreateAmendPartnerIncomeRequestData

import scala.concurrent.Future

class CreateAmendPartnerIncomeConnectorSpec extends ConnectorSpec {

  private val nino: Nino = Nino("AA123456A")
  private val taxYear: TaxYear = TaxYear.fromMtd("2026-27")
  
  private val requestData = CreateAmendPartnerIncomeRequestData(nino, taxYear, fullRequestModel)
  
  "createAmendPartnerIncome" should {
    "return a success response when success received from downstream" in new Test {
      val expected: Right[Nothing, ResponseWrapper[Unit]] = Right(ResponseWrapper(correlationId, ()))
      
      willPut(url"$baseUrl/itsa/income-tax/v1/${taxYear.asTysDownstream}/income/partnerships/$nino", fullDownstreamJson)
        .returning(Future.successful(expected))
      
      val result: DownstreamOutcome[Unit] = await(connector.createAmendPartnerIncome(requestData))
      result shouldBe expected
    }

    "return an unsuccessful response when downstream returns an error" in new Test {
      val error: DownstreamErrors = DownstreamErrors.single(DownstreamErrorCode("INVALID_TAX_YEAR"))
      val expected: Left[ResponseWrapper[DownstreamErrors], Nothing] = Left(ResponseWrapper(correlationId, error))

      willPut(url"$baseUrl/itsa/income-tax/v1/${taxYear.asTysDownstream}/income/partnerships/$nino", fullDownstreamJson)
        .returning(Future.successful(expected))

      val result: DownstreamOutcome[Unit] = await(connector.createAmendPartnerIncome(requestData))
      result shouldBe expected
    }
  }
  
  trait Test extends HipTest {
    self: ConnectorTest =>
    val connector = new CreateAmendPartnerIncomeConnector(mockHttpClient, mockAppConfig)
  }
}
