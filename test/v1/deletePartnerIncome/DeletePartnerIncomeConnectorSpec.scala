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

package v1.deletePartnerIncome

import api.connectors.{ConnectorSpec, DownstreamOutcome}
import api.models.domain.{Nino, PartnershipUtr, TaxYear}
import api.models.errors.*
import api.models.outcomes.ResponseWrapper
import uk.gov.hmrc.http.StringContextOps
import v1.deletePartnerIncome.model.request.DeletePartnerIncomeRequestData

import scala.concurrent.Future

class DeletePartnerIncomeConnectorSpec extends ConnectorSpec {

  private val nino: String           = "AA123456A"
  private val partnershipUtr: String = "1234567890"
  private val taxYear: String        = "2026-27"

  val request: DeletePartnerIncomeRequestData =
    DeletePartnerIncomeRequestData(Nino(nino), TaxYear.fromMtd(taxYear), PartnershipUtr(partnershipUtr))

  "deletePartnerIncome" should {
    "return a success response" in new HipTest with Test {

      val expected: Right[Nothing, ResponseWrapper[Unit]] = Right(ResponseWrapper(correlationId, ()))

      willDelete(url = url"$baseUrl/itsa/income-tax/v1/26-27/income/partnerships/$nino?partnershipUTR=$partnershipUtr")
        .returning(Future.successful(expected))

      val result: DownstreamOutcome[Unit] = await(connector.deletePartnerIncome(request))
      result shouldBe expected
    }

    "return an unsuccessful response" when {
      "the downstream request is unsuccessful" in new HipTest with Test {
        val downstreamErrorResponse: DownstreamErrors                 = DownstreamErrors.single(DownstreamErrorCode("SOME_ERROR"))
        val outcome: Left[ResponseWrapper[DownstreamErrors], Nothing] = Left(ResponseWrapper(correlationId, downstreamErrorResponse))

        willDelete(url = url"$baseUrl/itsa/income-tax/v1/26-27/income/partnerships/$nino?partnershipUTR=$partnershipUtr")
          .returns(Future.successful(outcome))

        val result: DownstreamOutcome[Unit] = await(connector.deletePartnerIncome(request))
        result shouldBe outcome
      }
    }
  }

  trait Test {
    self: ConnectorTest =>
    val connector: DeletePartnerIncomeConnector = new DeletePartnerIncomeConnector(http = mockHttpClient, appConfig = mockAppConfig)
  }

}
