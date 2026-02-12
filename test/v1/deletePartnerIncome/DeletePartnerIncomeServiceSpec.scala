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

import api.models.domain.{Nino, PartnershipUtr, TaxYear}
import api.models.errors.*
import api.models.outcomes.ResponseWrapper
import api.services.ServiceSpec
import v1.deletePartnerIncome.model.request.DeletePartnerIncomeRequestData

import scala.concurrent.Future

class DeletePartnerIncomeServiceSpec extends ServiceSpec {

  private val nino: String           = "AA123456A"
  private val partnershipUtr: String = "1234567890"
  private val taxYear: String        = "2026-27"

  trait Test extends MockDeletePartnerIncomeConnector {
    lazy val service = new DeletePartnerIncomeService(connector)
  }

  lazy val request: DeletePartnerIncomeRequestData =
    DeletePartnerIncomeRequestData(Nino(nino), TaxYear.fromMtd(taxYear), PartnershipUtr(partnershipUtr))

  "deletePartnerIncome" should {
    "return a Right" when {
      "the connector call is successful" in new Test {
        val downstreamResponse: ResponseWrapper[Unit] = ResponseWrapper(correlationId, ())
        val expected: ResponseWrapper[Unit]           = ResponseWrapper(correlationId, ())
        MockDeletePartnerIncomeConnector.deletePartnerIncome(request).returns(Future.successful(Right(downstreamResponse)))

        await(service.deletePartnerIncome(request)) shouldBe Right(expected)
      }
    }

    "return that wrapped error as-is" when {
      "the connector returns an outbound error" in new Test {
        val someError: MtdError                                = MtdError("SOME_CODE", "some message", BAD_REQUEST)
        val downstreamResponse: ResponseWrapper[OutboundError] = ResponseWrapper(correlationId, OutboundError(someError))
        MockDeletePartnerIncomeConnector.deletePartnerIncome(request).returns(Future.successful(Left(downstreamResponse)))

        await(service.deletePartnerIncome(request)) shouldBe Left(ErrorWrapper(correlationId, someError, None))
      }
    }

    "map errors according to spec" when {
      def serviceError(downstreamErrorCode: String, error: MtdError): Unit =
        s"a $downstreamErrorCode error is returned from the service" in new Test {
          MockDeletePartnerIncomeConnector
            .deletePartnerIncome(request)
            .returns(Future.successful(Left(ResponseWrapper(correlationId, DownstreamErrors.single(DownstreamErrorCode(downstreamErrorCode))))))

          await(service.deletePartnerIncome(request)) shouldBe Left(ErrorWrapper(correlationId, error))
        }

      val errors = List(
        ("INVALID_TAXABLE_ENTITY_ID", NinoFormatError),
        ("INVALID_TAX_YEAR", TaxYearFormatError),
        ("INVALID_CORRELATION_ID", InternalError),
        ("INVALID_PARTNERSHIP_UTR", PartnershipUtrFormatError),
        ("NO_DATA_FOUND", NotFoundError),
        ("TAX_YEAR_NOT_SUPPORTED", InternalError),
        ("OUTSIDE_AMENDMENT_WINDOW", RuleOutsideAmendmentWindowError),
        ("SERVER_ERROR", InternalError),
        ("SERVICE_UNAVAILABLE", InternalError)
      )

      errors.foreach(args => serviceError.tupled(args))
    }

  }

}
