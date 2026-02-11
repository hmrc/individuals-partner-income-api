package v1.list

import api.connectors.DownstreamOutcome
import org.scalamock.handlers.CallHandler
import org.scalamock.scalatest.MockFactory
import org.scalatest.TestSuite
import uk.gov.hmrc.http.HeaderCarrier
import v1.list.model.request.ListPartnerIncomeRequestData
import v1.list.model.response.ListPartnerIncomeResponse

import scala.concurrent.{ExecutionContext, Future}

trait MockListPartnerIncomeConnector extends TestSuite with MockFactory {
  val mockConnector: ListPartnerIncomeConnector = mock[ListPartnerIncomeConnector]

  object MockedListPartnerIncomeConnector {

    def listPartnerIncome(request: ListPartnerIncomeRequestData): CallHandler[Future[DownstreamOutcome[ListPartnerIncomeResponse]]] =
      (mockConnector
        .listPartnerIncome(_: ListPartnerIncomeRequestData)(_: HeaderCarrier, _: ExecutionContext, _: String))
        .expects(request, *, *, *)

  }

}
