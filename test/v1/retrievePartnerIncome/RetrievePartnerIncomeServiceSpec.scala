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

package v1.retrievePartnerIncome

import api.models.errors.*
import api.models.outcomes.ResponseWrapper
import api.services.ServiceSpec
import v1.retrievePartnerIncome.RetrievePartnerIncomeFixtures.*
import v1.retrievePartnerIncome.model.response.RetrievePartnerIncomeResponse

import scala.concurrent.Future

class RetrievePartnerIncomeServiceSpec extends ServiceSpec {

  trait Test extends MockRetrievePartnerIncomeConnector {
    lazy val service = new RetrievePartnerIncomeService(connector)
  }

  "retrievePartnerIncome" should {
    "return a Right" when {
      "the connector call is successful" in new Test {
        val downstreamResponse: ResponseWrapper[RetrievePartnerIncomeResponse] = ResponseWrapper(correlationId, responseModel)
        val expected: ResponseWrapper[RetrievePartnerIncomeResponse]           = ResponseWrapper(correlationId, responseModel)
        MockRetrievePartnerIncomeConnector.retrievePartnerIncome(request).returns(Future.successful(Right(downstreamResponse)))

        await(service.retrievePartnerIncome(request)) shouldBe Right(expected)
      }
    }

    "return that wrapped error as-is" when {
      "the connector returns an outbound error" in new Test {
        val someError: MtdError                                = MtdError("SOME_CODE", "some message", BAD_REQUEST)
        val downstreamResponse: ResponseWrapper[OutboundError] = ResponseWrapper(correlationId, OutboundError(someError))
        MockRetrievePartnerIncomeConnector.retrievePartnerIncome(request).returns(Future.successful(Left(downstreamResponse)))

        await(service.retrievePartnerIncome(request)) shouldBe Left(ErrorWrapper(correlationId, someError, None))
      }
    }

    "map errors according to spec" when {
      def serviceError(downstreamErrorCode: String, error: MtdError): Unit =
        s"a $downstreamErrorCode error is returned from the service" in new Test {
          MockRetrievePartnerIncomeConnector
            .retrievePartnerIncome(request)
            .returns(Future.successful(Left(ResponseWrapper(correlationId, DownstreamErrors.single(DownstreamErrorCode(downstreamErrorCode))))))

          await(service.retrievePartnerIncome(request)) shouldBe Left(ErrorWrapper(correlationId, error))
        }

      val errors = List(
        ("INVALID_TAXABLE_ENTITY_ID", NinoFormatError),
        ("INVALID_TAX_YEAR", TaxYearFormatError),
        ("INVALID_CORRELATION_ID", InternalError),
        ("INVALID_PARTNERSHIP_UTR", PartnershipUtrFormatError),
        ("NO_DATA_FOUND", NotFoundError),
        ("TAX_YEAR_NOT_SUPPORTED", InternalError),
        ("SERVER_ERROR", InternalError),
        ("SERVICE_UNAVAILABLE", InternalError)
      )

      errors.foreach(args => serviceError.tupled(args))
    }

  }

}
