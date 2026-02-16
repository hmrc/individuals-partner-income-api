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

package v1.listPartnerIncome

import api.controllers.RequestContext
import api.services.ServiceOutcome
import org.scalamock.handlers.CallHandler
import org.scalamock.scalatest.MockFactory
import org.scalatest.TestSuite
import v1.listPartnerIncome.model.request.ListPartnerIncomeRequestData
import v1.listPartnerIncome.model.response.ListPartnerIncomeResponse

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
