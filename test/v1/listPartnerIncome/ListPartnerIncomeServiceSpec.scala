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

import api.models.errors.*
import api.models.outcomes.ResponseWrapper
import api.services.{ServiceOutcome, ServiceSpec}
import v1.listPartnerIncome.ListPartnerIncomeFixtures.*
import v1.listPartnerIncome.model.response.ListPartnerIncomeResponse

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
        "TAX_YEAR_NOT_SUPPORTED"    -> InternalError
      )

      errors.foreach(serviceError.tupled)
    }
  }

}
