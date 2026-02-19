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
import api.models.utils.JsonErrorValidators
import api.utils.UnitSpec
import play.api.libs.json.{JsObject, JsString, JsValue}
import v1.createAmendPartnerIncome.CreateAmendPartnerIncomeFixtures.*
import v1.createAmendPartnerIncome.model.request.CreateAmendPartnerIncomeRequestData

class CreateAmendPartnerIncomeValidatorSpec extends UnitSpec with JsonErrorValidators {

  private def validator(nino: String, taxYear: String, body: JsValue) = {
    new CreateAmendPartnerIncomeValidator(nino, taxYear, body)
  }

  private implicit val correlationId: String = "1234"

  "validator" should {
    "return the parsed domain object" when {
      "given a full valid request" in {
        val result: Either[ErrorWrapper, CreateAmendPartnerIncomeRequestData] =
          validator(nino.nino, taxYear.asMtd, fullMtdJson).validateAndWrapResult()
        result shouldBe Right(request)
      }

      "given a valid request with no startDate, endDate or cashBasis" in {
        val json = fullMtdJson.as[JsObject] - "startDate" - "endDate" - "cashBasis"
        val result: Either[ErrorWrapper, CreateAmendPartnerIncomeRequestData] =
          validator(nino.nino, taxYear.asMtd, json).validateAndWrapResult()

        result shouldBe Right(
          CreateAmendPartnerIncomeRequestData(nino, taxYear, fullRequestModel.copy(startDate = None, endDate = None, cashBasis = None)))
      }
    }

    "return a single error" when {
      "passed an invalid nino" in {
        val result = validator("nino", taxYear.asMtd, fullMtdJson).validateAndWrapResult()
        result shouldBe Left(ErrorWrapper(correlationId, NinoFormatError))
      }

      "passed an invalid taxYear" in {
        val result = validator(nino.nino, "invalid", fullMtdJson).validateAndWrapResult()
        result shouldBe Left(ErrorWrapper(correlationId, TaxYearFormatError))
      }

      "passed an invalid taxYear range" in {
        val result = validator(nino.nino, "2026-28", fullMtdJson).validateAndWrapResult()
        result shouldBe Left(ErrorWrapper(correlationId, RuleTaxYearRangeInvalidError))
      }

      "passed an unsupported taxYear" in {
        val result = validator(nino.nino, "2025-26", fullMtdJson).validateAndWrapResult()
        result shouldBe Left(ErrorWrapper(correlationId, RuleTaxYearNotSupportedError))
      }

      "passed a body containing an invalid partnershipUtr" in {
        val json   = fullMtdJson.update("/partnershipUtr", JsString("invalid"))
        val result = validator(nino.nino, "2026-27", json).validateAndWrapResult()
        result shouldBe Left(ErrorWrapper(correlationId, PartnershipUtrFormatError.withPath("/partnershipUtr")))
      }

      "passed a body containing an invalid partnershipName" in {
        val json = fullMtdJson.update("/partnershipName", JsString("x" * 107))
        val result = validator(nino.nino, "2026-27", json).validateAndWrapResult()
        result shouldBe Left(ErrorWrapper(correlationId, PartnershipNameFormatError.withPath("/partnershipName")))
      }

      "passed a body containing an invalid startDate" in {
        val json = fullMtdJson.update("/startDate", JsString("invalid"))
        val result = validator(nino.nino, "2026-27", json).validateAndWrapResult()
        result shouldBe Left(ErrorWrapper(correlationId, StartDateFormatError.withPath("/startDate")))
      }

      "passed a body containing an invalid endDate" in {
        val json = fullMtdJson.update("/endDate", JsString("invalid"))
        val result = validator(nino.nino, "2026-27", json).validateAndWrapResult()
        result shouldBe Left(ErrorWrapper(correlationId, EndDateFormatError.withPath("/endDate")))
      }

      "passed a body containing a start date that is after the end date" in {
        val json = fullMtdJson.update("/startDate", JsString("2026-07-25"))
        val result = validator(nino.nino, "2026-27", json).validateAndWrapResult()
        result shouldBe Left(ErrorWrapper(correlationId, RuleEndBeforeStartDateError))
      }

      "passed a body containing a start date that is falls outside the taxYear" in {
        val json = fullMtdJson.removeProperty("/endDate").update("/startDate", JsString("2027-07-25"))
        println(json)
        val result = validator(nino.nino, "2026-27", json).validateAndWrapResult()
        result shouldBe Left(ErrorWrapper(correlationId, RuleStartDateError.withPath("/startDate")))
      }

      "passed a body containing an end date that falls outside the taxYear" in {
        val json = fullMtdJson.update("/endDate", JsString("2027-07-25"))
        val result = validator(nino.nino, "2026-27", json).validateAndWrapResult()
        result shouldBe Left(ErrorWrapper(correlationId, RuleEndDateError.withPath("/endDate")))
      }



    }

    "return multiple error" when {
      "passed multiple invalid fields" in {
        val result = validator("invalid", "invalid", fullMtdJson).validateAndWrapResult()
        result shouldBe Left(
          ErrorWrapper(
            correlationId,
            BadRequestError,
            Some(List(NinoFormatError, TaxYearFormatError))
          )
        )
      }
    }
  }

}
