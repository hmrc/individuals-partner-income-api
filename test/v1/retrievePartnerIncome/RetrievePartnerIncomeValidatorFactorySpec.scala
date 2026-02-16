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

import api.models.domain.{Nino, PartnershipUtr, TaxYear}
import api.models.errors.*
import api.models.utils.JsonErrorValidators
import api.utils.UnitSpec
import v1.retrievePartnerIncome.model.request.RetrievePartnerIncomeRequestData

class RetrievePartnerIncomeValidatorFactorySpec extends UnitSpec with JsonErrorValidators {

  private val validNino           = "AA123456A"
  private val validPartnershipUtr = "1234567890"
  private val validTaxYear        = "2026-27"

  private implicit val correlationId: String = "1234"
  private val validatorFactory               = new RetrievePartnerIncomeValidatorFactory

  private val parsedNino           = Nino(validNino)
  private val parsedPartnershipUtr = PartnershipUtr(validPartnershipUtr)
  private val parsedTaxYear        = TaxYear.fromMtd(validTaxYear)

  "running a validation" should {
    "return the parsed domain object" when {
      "given a valid request" in {
        val result = validatorFactory.validator(validNino, validTaxYear, validPartnershipUtr).validateAndWrapResult()
        result shouldBe Right(RetrievePartnerIncomeRequestData(parsedNino, parsedTaxYear, parsedPartnershipUtr))
      }
    }

    "return NinoFormatError" when {
      "given an invalid Nino" in {
        val result = validatorFactory.validator("invalid-nino", validTaxYear, validPartnershipUtr).validateAndWrapResult()
        result shouldBe Left(
          ErrorWrapper(correlationId, NinoFormatError)
        )
      }
    }

    "return RuleTaxYearNotSupportedError" when {
      "given an unsupported tax year" in {
        val result = validatorFactory.validator(validNino, "2025-26", validPartnershipUtr).validateAndWrapResult()
        result shouldBe Left(
          ErrorWrapper(correlationId, RuleTaxYearNotSupportedError)
        )
      }
    }

    "return RuleTaxYearRangeInvalidError" when {
      "given a tax year range which isn't a single year" in {
        val result = validatorFactory.validator(validNino, "2025-27", validPartnershipUtr).validateAndWrapResult()
        result shouldBe Left(
          ErrorWrapper(correlationId, RuleTaxYearRangeInvalidError)
        )
      }
    }

    "return PartnershipUtrFormatError" when {
      "given an invalid partnership UTR" in {
        val result = validatorFactory.validator(validNino, validTaxYear, "abc").validateAndWrapResult()
        result shouldBe Left(
          ErrorWrapper(correlationId, PartnershipUtrFormatError)
        )
      }
    }

    "return multiple errors" when {
      "given multiple invalid request parameters" in {
        val result = validatorFactory.validator("invalid-nino", "invalid-tax-year", "abc").validateAndWrapResult()
        result shouldBe Left(ErrorWrapper(correlationId, BadRequestError, Some(List(NinoFormatError, PartnershipUtrFormatError, TaxYearFormatError))))
      }
    }

  }

}
