package v1.list

import api.connectors.ConnectorSpec
import api.models.outcomes.ResponseWrapper
import uk.gov.hmrc.http.StringContextOps
import v1.list.ListPartnerIncomeFixtures.*
import v1.list.model.response.ListPartnerIncomeResponse

import scala.concurrent.Future

class ListPartnerIncomeConnectorSpec extends ConnectorSpec {
  
  private val response = Right(ResponseWrapper(correlationId, responseModel))

  "listPartnerIncome" should {
    "return a successful response" in new HipTest with Test {
      willGet(url"$baseUrl/itsa/income-tax/v1/26-27/income/partnerships/$nino/list")
        .returning(Future.successful(response))

      await(connector.listPartnerIncome(request)) shouldBe response
    }
  }

  trait Test { self: ConnectorTest =>
    val connector = new ListPartnerIncomeConnector(mockHttpClient, mockAppConfig)
  }
}
