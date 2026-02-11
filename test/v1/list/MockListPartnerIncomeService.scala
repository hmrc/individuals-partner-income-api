package v1.list

import api.controllers.RequestContext
import api.services.ServiceOutcome
import org.scalamock.handlers.CallHandler
import org.scalamock.scalatest.MockFactory
import org.scalatest.TestSuite
import v1.list.model.request.ListPartnerIncomeRequestData
import v1.list.model.response.ListPartnerIncomeResponse

import scala.concurrent.{ExecutionContext, Future}

trait MockListPartnerIncomeService extends TestSuite with MockFactory {

  val mockService: ListPartnerIncomeService = mock[ListPartnerIncomeService]

  object MockedListPartnerIncomeService {

    def listPartnerIncome(request: ListPartnerIncomeRequestData): CallHandler[Future[ServiceOutcome[ListPartnerIncomeResponse]]] =
      (mockService
        .listPartnerIncome(_: ListPartnerIncomeRequestData)(_: RequestContext, _: ExecutionContext))
        .expects(request, *, *)

  }

}
