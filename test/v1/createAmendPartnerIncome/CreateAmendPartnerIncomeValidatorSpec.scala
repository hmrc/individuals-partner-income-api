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
import play.api.libs.json.{JsArray, JsNumber, JsObject, JsString, JsValue, Json}
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
        val result = validator(nino.nino, taxYear.asMtd, json).validateAndWrapResult()
        result shouldBe Left(ErrorWrapper(correlationId, PartnershipUtrFormatError.withPath("/partnershipUtr")))
      }

      "passed a body containing an invalid partnershipName" in {
        val json   = fullMtdJson.update("/partnershipName", JsString("x" * 107))
        val result = validator(nino.nino, taxYear.asMtd, json).validateAndWrapResult()
        result shouldBe Left(ErrorWrapper(correlationId, PartnershipNameFormatError.withPath("/partnershipName")))
      }

      "passed a body containing an invalid startDate" in {
        val json   = fullMtdJson.update("/startDate", JsString("invalid"))
        val result = validator(nino.nino, taxYear.asMtd, json).validateAndWrapResult()
        result shouldBe Left(ErrorWrapper(correlationId, StartDateFormatError.withPath("/startDate")))
      }

      "passed a body containing an invalid endDate" in {
        val json   = fullMtdJson.update("/endDate", JsString("invalid"))
        val result = validator(nino.nino, taxYear.asMtd, json).validateAndWrapResult()
        result shouldBe Left(ErrorWrapper(correlationId, EndDateFormatError.withPath("/endDate")))
      }

      "passed a body containing a start date that is after the end date" in {
        val json   = fullMtdJson.update("/startDate", JsString("2026-07-25"))
        val result = validator(nino.nino, taxYear.asMtd, json).validateAndWrapResult()
        result shouldBe Left(ErrorWrapper(correlationId, RuleEndBeforeStartDateError))
      }

      "passed a body containing a start date that falls outside the taxYear" in {
        val json   = fullMtdJson.removeProperty("/endDate").update("/startDate", JsString("2027-07-25"))
        val result = validator(nino.nino, taxYear.asMtd, json).validateAndWrapResult()
        result shouldBe Left(ErrorWrapper(correlationId, RuleStartDateError.withPath("/startDate")))
      }

      "passed a body containing an end date that falls outside the taxYear" in {
        val json   = fullMtdJson.update("/endDate", JsString("2027-07-25"))
        val result = validator(nino.nino, taxYear.asMtd, json).validateAndWrapResult()
        result shouldBe Left(ErrorWrapper(correlationId, RuleEndDateError.withPath("/endDate")))
      }

      "passed a body that is missing mandatory fields" in {
        val json   = fullMtdJson.removeProperty("/partnershipName").removeProperty("/partnershipUtr")
        val result = validator(nino.nino, taxYear.asMtd, json).validateAndWrapResult()
        result shouldBe Left(ErrorWrapper(correlationId, RuleIncorrectOrEmptyBodyError.withPaths(Seq("/partnershipName", "/partnershipUtr"))))
      }

      "passed a body containing an invalid trade description" in {
        val replacement = Json.parse(s"""
             |[
             |   {
             |      "tradeDescription": "${"x" * 32}",
             |      "tradingOrProfessionalProfits": {
             |        "shareOfProfitOrLoss": 5000.99,
             |        "adjustedProfit": 5000.99,
             |        "shareOfTaxableProfit": 5000.99
             |     }
             |   }
             | ]
             |""".stripMargin)
        val json   = fullMtdJson.update("partnershipTrades", replacement)
        val result = validator(nino.nino, taxYear.asMtd, json).validateAndWrapResult()
        result shouldBe Left(ErrorWrapper(correlationId, TradeDescriptionFormatError.withPath("/partnershipTrades/0/tradeDescription")))
      }

      "passed a body containing multiple invalid trade descriptions" in {
        val replacement = Json.parse(s"""
             |[
             |   {
             |      "tradeDescription": "${"x" * 32}",
             |      "tradingOrProfessionalProfits": {
             |        "shareOfProfitOrLoss": 5000.99,
             |        "adjustedProfit": 5000.99,
             |        "shareOfTaxableProfit": 5000.99
             |     }
             |   },
             |      {
             |      "tradeDescription": "${"y" * 32}",
             |      "tradingOrProfessionalProfits": {
             |        "shareOfProfitOrLoss": 5000.99,
             |        "adjustedProfit": 5000.99,
             |        "shareOfTaxableProfit": 5000.99
             |     }
             |   }
             | ]
             |""".stripMargin)
        val json   = fullMtdJson.update("partnershipTrades", replacement)
        val result = validator(nino.nino, taxYear.asMtd, json).validateAndWrapResult()
        result shouldBe Left(
          ErrorWrapper(
            correlationId,
            TradeDescriptionFormatError.withPaths(Seq("/partnershipTrades/0/tradeDescription", "/partnershipTrades/1/tradeDescription"))))
      }

      "passed a body containing duplicate trade descriptions" in {
        val replacement = Json.parse(s"""
             |[
             |  {
             |    "tradeDescription": "Consultancy Services",
             |    "tradingOrProfessionalProfits": {
             |      "shareOfProfitOrLoss": 5000.99,
             |      "adjustedProfit": 5000.99,
             |      "shareOfTaxableProfit": 5000.99
             |    }
             |  },
             |  {
             |    "tradeDescription": "Consultancy Services",
             |    "tradingOrProfessionalProfits": {
             |      "shareOfProfitOrLoss": 5000.99,
             |      "adjustedProfit": 5000.99,
             |      "shareOfTaxableProfit": 5000.99
             |    }
             |  }
             |]
             |""".stripMargin)
        val json   = fullMtdJson.update("partnershipTrades", replacement)
        val result = validator(nino.nino, taxYear.asMtd, json).validateAndWrapResult()
        result shouldBe Left(
          ErrorWrapper(
            correlationId,
            RuleDuplicateTradeDescriptionError.withPaths(Seq("/partnershipTrades/0/tradeDescription", "/partnershipTrades/1/tradeDescription"))))
      }

      "passed a body missing partnershipTrade details" in {
        val replacement = Json.parse(s"""
             |[
             |  {
             |    "tradeDescription": "Consultancy Services"
             |  }
             |]
             |""".stripMargin)
        val json   = fullMtdJson.update("partnershipTrades", replacement)
        val result = validator(nino.nino, taxYear.asMtd, json).validateAndWrapResult()
        result shouldBe Left(ErrorWrapper(correlationId, RuleMissingPartnershipTradesDetailsError.withPath("/partnershipTrades/0")))
      }

      def testWith(error: MtdError)(body: JsValue, path: String): Unit = {
        s"return an error for $path" in {
          val result: Either[ErrorWrapper, CreateAmendPartnerIncomeRequestData] =
            validator(nino.nino, taxYear.asMtd, body).validateAndWrapResult()

          result shouldBe Left(ErrorWrapper(correlationId, error.withPath(path)))
        }
      }

      def testValueFormatErrorWith(body: JsValue, path: String): Unit = testWith(ValueFormatError)(body, path)

      def testValueFormatErrorWithNegativeValue(body: JsValue, path: String): Unit =
        testWith(ValueFormatError.forPathAndRange(path, "-99999999999.99", "99999999999.99"))(body, path)

      def updatePath(json: JsValue, pathStr: String, replacement: JsValue): JsValue = {
        val parts: List[String] =
          pathStr.stripPrefix("/").split('/').toList.filter(_.nonEmpty)

        def applyAt(js: JsValue, remaining: List[String]): JsValue =
          (js, remaining) match {
            case (_, Nil) =>
              replacement
            case (obj: JsObject, key :: tail) if key != "0" =>
              obj.value.get(key) match {
                case Some(child) =>
                  obj + (key -> applyAt(child, tail))
                case None =>
                  obj
              }
            case (JsArray(values), "0" :: tail) =>
              JsArray(values.map(elem => applyAt(elem, tail)))
            case (other, _) =>
              other
          }

        applyAt(json, parts)
      }

      "passed a body with invalid numeric values" should {

        List(
          "/partnershipTrades/0/tradingOrProfessionalProfits/accountingAdjustment",
          "/partnershipTrades/0/tradingOrProfessionalProfits/foreignTaxClaimedDeduction",
          "/partnershipTrades/0/tradingOrProfessionalProfits/adjustedProfit",
          "/partnershipTrades/0/tradingOrProfessionalProfits/transitionProfitArisingThisYear",
          "/partnershipTrades/0/tradingOrProfessionalProfits/lossesBroughtForwardTransitionProfit",
          "/partnershipTrades/0/tradingOrProfessionalProfits/lossesBroughtForwardAdjustedProfit",
          "/partnershipTrades/0/tradingOrProfessionalProfits/profitAfterBroughtForwardLosses",
          "/partnershipTrades/0/tradingOrProfessionalProfits/otherBusinessIncome",
          "/partnershipTrades/0/tradingOrProfessionalProfits/shareOfTaxableProfit",
          "/partnershipTrades/0/tradingOrProfessionalLosses/adjustedLoss",
          "/partnershipTrades/0/tradingOrProfessionalLosses/currentYearLossAppliedToGeneralIncome",
          "/partnershipTrades/0/tradingOrProfessionalLosses/lossesCarriedBack",
          "/partnershipTrades/0/tradingOrProfessionalLosses/carryForwardLosses"
        ).foreach(path =>
          val json = updatePath(fullMtdJson, path, JsNumber(123.456))
          testValueFormatErrorWith(json, path)
        )

        List(
          "/partnershipTrades/0/tradingOrProfessionalProfits/shareOfProfitOrLoss",
          "/partnershipTrades/0/tradingOrProfessionalProfits/basisAdjustment",
          "/partnershipTrades/0/tradingOrProfessionalProfits/averagingAdjustment"
        ).foreach(path =>
          val json = updatePath(fullMtdJson, path, JsNumber(123.456))
          testValueFormatErrorWithNegativeValue(json, path)
        )

        List(
          "/nationalInsuranceContributions/adjustmentToProfitsForClass4",
          "/untaxedSavingsIncome/shareOfUkUntaxedSavingsIncome",
          "/untaxedSavingsIncome/adjustedUkSavingsIncome",
          "/untaxedSavingsIncome/shareOfForeignUntaxedSavingsIncome",
          "/untaxedSavingsIncome/totalForeignTaxTakenOff",
          "/untaxedSavingsIncome/adjustedForeignSavingsIncome",
          "/untaxedSavingsIncome/totalUntaxedSavingsIncome",
          "/ukPropertyIncome/lossesBroughtForward",
          "/ukPropertyIncome/currentYearLossAppliedToGeneralIncome",
          "/ukPropertyIncome/carryForwardLosses",
          "/ukPropertyIncome/shareOfTaxableProfit",
          "/ukPropertyIncome/residentialPropertyFinanceCosts",
          "/ukPropertyIncome/residentialPropertyFinanceCostsBroughtForward",
          "/otherUntaxedUkIncome/shareOfOtherUntaxedUkIncome",
          "/otherUntaxedUkIncome/lossesBroughtForward",
          "/otherUntaxedUkIncome/shareOfTaxableProfit",
          "/otherUntaxedUkIncome/shareOfLoss",
          "/otherUntaxedUkIncome/carryForwardLosses",
          "/offshoreFundsIncome/shareOfOffshoreFundsIncome",
          "/offshoreFundsIncome/totalForeignTaxTakenOff",
          "/offshoreFundsIncome/shareOfTaxableIncome",
          "/otherUntaxedForeignIncome/shareOfOtherUntaxedForeignIncome",
          "/otherUntaxedForeignIncome/lossesBroughtForward",
          "/otherUntaxedForeignIncome/totalForeignTaxTakenOff",
          "/otherUntaxedForeignIncome/shareOfTaxableProfit",
          "/otherUntaxedForeignIncome/shareOfLoss",
          "/otherUntaxedForeignIncome/carryForwardLosses",
          "/otherUntaxedForeignIncome/residentialPropertyFinanceCosts",
          "/otherUntaxedForeignIncome/residentialPropertyFinanceCostsBroughtForward",
          "/totalUntaxedIncomeExcludingSavings/shareOfTotalUntaxedIncomeExcludingSavings",
          "/taxedIncomeAndDividendIncome/shareOfDividendIncome",
          "/taxedIncomeAndDividendIncome/dividendIncomeForeignTaxTakenOff",
          "/taxedIncomeAndDividendIncome/totalDividendIncome",
          "/taxedIncomeAndDividendIncome/shareOfTaxedIncome",
          "/taxedIncomeAndDividendIncome/taxedIncomeForeignTaxTakenOff",
          "/taxedIncomeAndDividendIncome/totalTaxedIncome",
          "/taxedIncomeAndDividendIncome/shareOfOtherTaxedIncome",
          "/taxedIncomeAndDividendIncome/otherTaxedIncomeForeignTaxTakenOff",
          "/totalTaxedAndUntaxedIncome/shareOfTotalTaxedAndUntaxedIncome",
          "/taxPaidAndDeductions/shareOfIncomeTaxTakenOffPartnershipIncome",
          "/taxPaidAndDeductions/shareOfCisDeductionsByContractors",
          "/taxPaidAndDeductions/shareOfTaxTakenOffTradingIncome",
          "/taxPaidAndDeductions/shareOfTotalTaxTakenOff"
        ).foreach(path => testValueFormatErrorWith(fullMtdJson.update(path, JsNumber(123.456)), path))

        List(
          "/untaxedSavingsIncome/untaxedSavingsIncomeAdjustment",
          "/untaxedSavingsIncome/foreignUntaxedSavingsIncomeAdjustment",
          "/ukPropertyIncome/shareOfProfitOrLoss",
          "/ukPropertyIncome/profitOrLossAdjustment",
          "/otherUntaxedUkIncome/otherUntaxedUkIncomeAdjustment",
          "/otherUntaxedUkIncome/otherUntaxedUkIncomeLossAdjustment",
          "/offshoreFundsIncome/offshoreFundsIncomeAdjustment",
          "/otherUntaxedForeignIncome/otherUntaxedForeignIncomeAdjustment",
          "/otherUntaxedForeignIncome/otherUntaxedForeignIncomeLossesAdjustment"
        ).foreach(path => testValueFormatErrorWithNegativeValue(fullMtdJson.update(path, JsNumber(123.456)), path))
      }

      "passed a body with multiple invalid fields" in {
        val path0 = "/untaxedSavingsIncome/untaxedSavingsIncomeAdjustment"
        val path1 = "/untaxedSavingsIncome/foreignUntaxedSavingsIncomeAdjustment"
        val path2 = "/ukPropertyIncome/shareOfProfitOrLoss"

        val invalidJson = fullMtdJson
          .update(path0, JsNumber(123.456))
          .update(path1, JsNumber(123.456))
          .update(path2, JsNumber(123.456))

        val result = validator(nino.nino, taxYear.asMtd, invalidJson).validateAndWrapResult()

        result shouldBe Left(
          ErrorWrapper(
            correlationId,
            ValueFormatError.forPathAndRange(path0, "-99999999999.99", "99999999999.99").withPaths(List(path0, path1, path2))))
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
