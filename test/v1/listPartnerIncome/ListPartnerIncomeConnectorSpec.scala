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

import api.connectors.ConnectorSpec
import api.models.outcomes.ResponseWrapper
import uk.gov.hmrc.http.StringContextOps
import v1.listPartnerIncome.ListPartnerIncomeFixtures.*
import v1.listPartnerIncome.model.response.ListPartnerIncomeResponse

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
