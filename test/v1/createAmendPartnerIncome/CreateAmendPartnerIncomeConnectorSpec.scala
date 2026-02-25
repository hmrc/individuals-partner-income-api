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

package v1.createAmendPartnerIncome

import api.connectors.{ConnectorSpec, DownstreamOutcome}
import api.models.errors.{DownstreamErrorCode, DownstreamErrors}
import api.models.outcomes.ResponseWrapper
import uk.gov.hmrc.http.StringContextOps
import v1.createAmendPartnerIncome.CreateAmendPartnerIncomeFixtures.{fullDownstreamJson, nino, request, taxYear}

import scala.concurrent.Future

class CreateAmendPartnerIncomeConnectorSpec extends ConnectorSpec {

  "createAmendPartnerIncome" should {
    "return a success response when success received from downstream" in new Test {
      val expected: Right[Nothing, ResponseWrapper[Unit]] = Right(ResponseWrapper(correlationId, ()))

      willPut(url"$baseUrl/itsa/income-tax/v1/${taxYear.asTysDownstream}/income/partnerships/$nino", fullDownstreamJson)
        .returning(Future.successful(expected))

      val result: DownstreamOutcome[Unit] = await(connector.createAmendPartnerIncome(request))
      result shouldBe expected
    }

    "return an unsuccessful response when downstream returns an error" in new Test {
      val error: DownstreamErrors                                    = DownstreamErrors.single(DownstreamErrorCode("INVALID_TAX_YEAR"))
      val expected: Left[ResponseWrapper[DownstreamErrors], Nothing] = Left(ResponseWrapper(correlationId, error))

      willPut(url"$baseUrl/itsa/income-tax/v1/${taxYear.asTysDownstream}/income/partnerships/$nino", fullDownstreamJson)
        .returning(Future.successful(expected))

      val result: DownstreamOutcome[Unit] = await(connector.createAmendPartnerIncome(request))
      result shouldBe expected
    }
  }

  trait Test extends HipTest {
    self: ConnectorTest =>
    val connector = new CreateAmendPartnerIncomeConnector(mockHttpClient, mockAppConfig)
  }

}
