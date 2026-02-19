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

import api.models.errors.*
import api.models.outcomes.ResponseWrapper
import api.services.ServiceSpec
import v1.createAmendPartnerIncome.CreateAmendPartnerIncomeFixtures.request

import scala.concurrent.Future

class CreateAmendPartnerIncomeServiceSpec extends ServiceSpec {

  trait Test extends MockCreateAmendPartnerIncomeConnector {
    val service = new CreateAmendPartnerIncomeService(mockConnector)
  }

  "createAmendPartnerIncome" should {
    "return a Right when the connector call is successful" in new Test {
      private val response = Right(ResponseWrapper(correlationId, ()))
      MockCreateAmendPartnerIncomeConnector
        .createAmend(request)
        .returns(Future.successful(response))

      await(service.createAmendPartnerIncome(request)) shouldBe response
    }

    "map errors according to spec" when {
      def serviceError(downstreamErrorCode: String, error: MtdError): Unit =
        s"a $downstreamErrorCode error is returned from the service" in new Test {
          MockCreateAmendPartnerIncomeConnector
            .createAmend(request)
            .returns(Future.successful(Left(ResponseWrapper(correlationId, DownstreamErrors.single(DownstreamErrorCode(downstreamErrorCode))))))

          await(service.createAmendPartnerIncome(request)) shouldBe Left(ErrorWrapper(correlationId, error))
        }

      val errors = List(
        "INVALID_TAXABLE_ENTITY_ID"   -> NinoFormatError,
        "INVALID_TAX_YEAR"            -> TaxYearFormatError,
        "INVALID_CORRELATION_ID"      -> InternalError,
        "INVALID_PAYLOAD"             -> InternalError,
        "INVALID_START_DATE"          -> RuleStartDateError,
        "START_DATE_NOT_COMPATIBLE"   -> RuleEndBeforeStartDateError,
        "INVALID_END_DATE"            -> RuleEndDateError,
        "INCORRECT_TRADE_DESCRIPTION" -> RuleDuplicateTradeDescriptionError,
        "TAX_YEAR_NOT_SUPPORTED"      -> InternalError,
        "OUTSIDE_AMENDMENT_WINDOW"    -> RuleOutsideAmendmentWindowError
      )

      errors.foreach(serviceError.tupled)

    }
  }

}
