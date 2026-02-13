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

package v1.list

import api.models.errors.{
  BadRequestError,
  ErrorWrapper,
  NinoFormatError,
  RuleTaxYearNotSupportedError,
  RuleTaxYearRangeInvalidError,
  TaxYearFormatError
}
import api.utils.UnitSpec
import v1.list.ListPartnerIncomeFixtures.*
import v1.list.model.request.ListPartnerIncomeRequestData

class ListPartnerIncomeValidatorSpec extends UnitSpec {

  private def validator(nino: String, taxYear: String) =
    new ListPartnerIncomeValidator(nino, taxYear)

  private implicit val correlationId: String = "1234"

  "validator" should {
    "return the parsed domain object" when {
      "given a valid request" in {
        val result: Either[ErrorWrapper, ListPartnerIncomeRequestData] = validator(nino.nino, taxYear.asMtd).validateAndWrapResult()
        result shouldBe Right(request)
      }
    }

    "return a single error" when {
      "passed an invalid nino" in {
        val result = validator("nino", taxYear.asMtd).validateAndWrapResult()
        result shouldBe Left(ErrorWrapper(correlationId, NinoFormatError))
      }

      "passed an invalid taxYear" in {
        val result = validator(nino.nino, "invalid").validateAndWrapResult()
        result shouldBe Left(ErrorWrapper(correlationId, TaxYearFormatError))
      }

      "passed an invalid taxYear range" in {
        val result = validator(nino.nino, "2026-28").validateAndWrapResult()
        result shouldBe Left(ErrorWrapper(correlationId, RuleTaxYearRangeInvalidError))
      }

      "passed an unsupported taxYear" in {
        val result = validator(nino.nino, "2025-26").validateAndWrapResult()
        result shouldBe Left(ErrorWrapper(correlationId, RuleTaxYearNotSupportedError))
      }
    }

    "return multiple error" when {
      "passed multiple invalid fields" in {
        val result = validator("invalid", "invalid").validateAndWrapResult()
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
