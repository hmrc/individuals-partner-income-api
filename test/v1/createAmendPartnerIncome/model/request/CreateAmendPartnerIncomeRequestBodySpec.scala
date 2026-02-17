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

package v1.createAmendPartnerIncome.model.request

import api.utils.UnitSpec
import play.api.libs.json.Json

class CreateAmendPartnerIncomeRequestBodySpec extends UnitSpec {

  private val mtdJson = Json.parse(
    """
      |{
      |  "partnershipUtr": "4564564564",
      |  "partnershipName": "ABC Partnership",
      |  "startDate": "2026-06-24",
      |  "endDate": "2026-07-24",
      |  "cashBasis": true,
      |  "partnershipTrades": [
      |    {
      |      "tradeDescription": "Consultancy Services",
      |      "tradingOrProfessionalProfits": {
      |        "shareOfProfitOrLoss": 5000.99,
      |        "basisAdjustment": 5000.99,
      |        "accountingAdjustment": 5000.99,
      |        "averagingAdjustment": 5000.99,
      |        "foreignTaxClaimedDeduction": 5000.99,
      |        "adjustedProfit": 5000.99,
      |        "transitionProfitArisingThisYear": 5000.99,
      |        "lossesBroughtForwardTransitionProfit": 5000.99,
      |        "lossesBroughtForwardAdjustedProfit": 5000.99,
      |        "profitAfterBroughtForwardLosses": 5000.99,
      |        "otherBusinessIncome": 5000.99,
      |        "shareOfTaxableProfit": 5000.99
      |      },
      |      "tradingOrProfessionalLosses": {
      |        "adjustedLoss": 5000.99,
      |        "currentYearLossAppliedToGeneralIncome": 5000.99,
      |        "lossesCarriedBack": 5000.99,
      |        "carryForwardLosses": 5000.99
      |      }
      |    }
      |  ],
      |  "nationalInsuranceContributions": {
      |    "voluntaryClass2Nics": true,
      |    "class4Exemption": true,
      |    "adjustmentToProfitsForClass4": 5000.99
      |  },
      |  "untaxedSavingsIncome": {
      |    "shareOfUkUntaxedSavingsIncome": 5000.99,
      |    "untaxedSavingsIncomeAdjustment": 5000.99,
      |    "adjustedUkSavingsIncome": 5000.99,
      |    "shareOfForeignUntaxedSavingsIncome": 5000.99,
      |    "foreignUntaxedSavingsIncomeAdjustment": 5000.99,
      |    "totalForeignTaxTakenOff": 5000.99,
      |    "adjustedForeignSavingsIncome": 5000.99,
      |    "totalUntaxedSavingsIncome": 5000.99
      |  },
      |  "ukPropertyIncome": {
      |    "shareOfProfitOrLoss": 5000.99,
      |    "profitOrLossAdjustment": 5000.99,
      |    "lossesBroughtForward": 5000.99,
      |    "currentYearLossAppliedToGeneralIncome": 5000.99,
      |    "carryForwardLosses": 5000.99,
      |    "shareOfTaxableProfit": 5000.99,
      |    "residentialPropertyFinanceCosts": 5000.99,
      |    "residentialPropertyFinanceCostsBroughtForward": 5000.99
      |  },
      |  "otherUntaxedUkIncome": {
      |    "shareOfOtherUntaxedUkIncome": 5000.99,
      |    "otherUntaxedUkIncomeAdjustment": 5000.99,
      |    "lossesBroughtForward": 5000.99,
      |    "shareOfTaxableProfit": 5000.99,
      |    "shareOfLoss": 5000.99,
      |    "otherUntaxedUkIncomeLossAdjustment": 5000.99,
      |    "carryForwardLosses": 5000.99
      |  },
      |  "offshoreFundsIncome": {
      |    "shareOfOffshoreFundsIncome": 5000.99,
      |    "offshoreFundsIncomeAdjustment": 5000.99,
      |    "totalForeignTaxTakenOff": 5000.99,
      |    "shareOfTaxableIncome": 5000.99
      |  },
      |  "otherUntaxedForeignIncome": {
      |    "shareOfOtherUntaxedForeignIncome": 5000.99,
      |    "otherUntaxedForeignIncomeAdjustment": 5000.99,
      |    "lossesBroughtForward": 5000.99,
      |    "totalForeignTaxTakenOff": 5000.99,
      |    "shareOfTaxableProfit": 5000.99,
      |    "shareOfLoss": 5000.99,
      |    "otherUntaxedForeignIncomeLossesAdjustment": 5000.99,
      |    "carryForwardLosses": 5000.99,
      |    "residentialPropertyFinanceCosts": 5000.99,
      |    "residentialPropertyFinanceCostsBroughtForward": 5000.99
      |  },
      |  "totalUntaxedIncomeExcludingSavings": {
      |    "shareOfTotalUntaxedIncomeExcludingSavings": 5000.99
      |  },
      |  "taxedIncomeAndDividendIncome": {
      |    "shareOfDividendIncome": 5000.99,
      |    "dividendIncomeForeignTaxTakenOff": 5000.99,
      |    "totalDividendIncome": 5000.99,
      |    "shareOfTaxedIncome": 5000.99,
      |    "taxedIncomeForeignTaxTakenOff": 5000.99,
      |    "totalTaxedIncome": 5000.99,
      |    "shareOfOtherTaxedIncome": 5000.99,
      |    "otherTaxedIncomeForeignTaxTakenOff": 5000.99
      |  },
      |  "totalTaxedAndUntaxedIncome": {
      |    "shareOfTotalTaxedAndUntaxedIncome": 5000.99
      |  },
      |  "taxPaidAndDeductions": {
      |    "shareOfIncomeTaxTakenOffPartnershipIncome": 5000.99,
      |    "shareOfCisDeductionsByContractors": 5000.99,
      |    "shareOfTaxTakenOffTradingIncome": 5000.99,
      |    "shareOfTotalTaxTakenOff": 5000.99
      |  }
      |}
      |""".stripMargin
  )

  private val downstreamJson = Json.parse(
    """
      |{
      |  "partnershipUTR": "4564564564",
      |  "partnershipName": "ABC Partnership",
      |  "startDate": "2026-06-24",
      |  "endDate": "2026-07-24",
      |  "cashBasis": true,
      |  "partnershipTrades": [
      |    {
      |      "tradeDescription": "Consultancy Services",
      |      "tradingOrProfessionalProfits": {
      |        "shareOfProfitOrLoss": 5000.99,
      |        "basisAdjustment": 5000.99,
      |        "accountingAdjustment": 5000.99,
      |        "averagingAdjustment": 5000.99,
      |        "foreignTaxClaimedDeduction": 5000.99,
      |        "adjustedProfit": 5000.99,
      |        "transitionProfitArisingThisYear": 5000.99,
      |        "lossesBroughtForwardTransitionProfit": 5000.99,
      |        "lossesBroughtForwardAdjustedProfit": 5000.99,
      |        "profitAfterBroughtForwardLosses": 5000.99,
      |        "otherBusinessIncome": 5000.99,
      |        "shareOfTaxableProfit": 5000.99
      |      },
      |      "tradingOrProfessionalLosses": {
      |        "adjustedLoss": 5000.99,
      |        "currentYearLossAppliedToGeneralIncome": 5000.99,
      |        "lossesCarriedBack": 5000.99,
      |        "carryForwardLosses": 5000.99
      |      }
      |    }
      |  ],
      |  "nationalInsuranceContributions": {
      |    "voluntaryClass2Nics": true,
      |    "class4Exemption": true,
      |    "adjustmentToProfitsForClass4": 5000.99
      |  },
      |  "untaxedSavingsIncome": {
      |    "shareOfUkUntaxedSavingsIncome": 5000.99,
      |    "untaxedSavingsIncomeAdjustment": 5000.99,
      |    "adjustedUKSavingsIncome": 5000.99,
      |    "shareOfForeignUntaxedSavingsIncome": 5000.99,
      |    "foreignUntaxedSavingsIncomeAdjustment": 5000.99,
      |    "totalForeignTaxTakenOff": 5000.99,
      |    "adjustedForeignSavingsIncome": 5000.99,
      |    "totalUntaxedSavingsIncome": 5000.99
      |  },
      |  "ukPropertyIncome": {
      |    "shareOfProfitOrLoss": 5000.99,
      |    "profitOrLossAdjustment": 5000.99,
      |    "lossesBroughtForward": 5000.99,
      |    "currentYearLossAppliedToGeneralIncome": 5000.99,
      |    "carryForwardLosses": 5000.99,
      |    "shareOfTaxableProfit": 5000.99,
      |    "residentialPropertyFinanceCosts": 5000.99,
      |    "residentialPropertyFinanceCostsBroughtForward": 5000.99
      |  },
      |  "otherUntaxedUKIncome": {
      |    "shareOfOtherUntaxedUKIncome": 5000.99,
      |    "otherUntaxedUKIncomeAdjustment": 5000.99,
      |    "lossesBroughtForward": 5000.99,
      |    "shareOfTaxableProfit": 5000.99,
      |    "shareOfLoss": 5000.99,
      |    "otherUntaxedUKIncomeLossAdjustment": 5000.99,
      |    "carryForwardLosses": 5000.99
      |  },
      |  "offshoreFundsIncome": {
      |    "shareOfOffshoreFundsIncome": 5000.99,
      |    "offShoreFundsIncomeAdjustment": 5000.99,
      |    "totalForeignTaxTakenOff": 5000.99,
      |    "shareOfTaxableIncome": 5000.99
      |  },
      |  "otherUntaxedForeignIncome": {
      |    "shareOfOtherUntaxedForeignIncome": 5000.99,
      |    "otherUntaxedForeignIncomeAdjustment": 5000.99,
      |    "lossesBroughtForward": 5000.99,
      |    "totalForeignTaxTakenOff": 5000.99,
      |    "shareOfTaxableProfit": 5000.99,
      |    "shareOfLoss": 5000.99,
      |    "otherUntaxedForeignIncomeLossesAdjustment": 5000.99,
      |    "carryForwardLosses": 5000.99,
      |    "residentialPropertyFinanceCosts": 5000.99,
      |    "residentialPropertyFinanceCostsBroughtForward": 5000.99
      |  },
      |  "totalUntaxedIncomeExcludingSavings": {
      |    "shareOfTotalUntaxedIncomeExcludingSavings": 5000.99
      |  },
      |  "taxedIncomeAndDividendIncome": {
      |    "shareOfDividendIncome": 5000.99,
      |    "dividendIncomeForeignTaxTakenOff": 5000.99,
      |    "totalDividendIncome": 5000.99,
      |    "shareOfTaxedIncome": 5000.99,
      |    "taxedIncomeForeignTaxTakenOff": 5000.99,
      |    "totalTaxedIncome": 5000.99,
      |    "shareOfOtherTaxedIncome": 5000.99,
      |    "otherTaxedIncomeForeignTaxTakenOff": 5000.99
      |  },
      |  "totalTaxedAndUntaxedIncome": {
      |    "shareOfTotalTaxedAndUntaxedIncome": 5000.99
      |  },
      |  "taxPaidAndDeductions": {
      |    "shareOfIncomeTaxTakenOffPartnershipIncome": 5000.99,
      |    "shareOfCisDeductionsByContractors": 5000.99,
      |    "shareOfTaxTakenOffTradingIncome": 5000.99,
      |    "shareOfTotalTaxTakenOff": 5000.99
      |  }
      |}
      |""".stripMargin
  )

  private val model = CreateAmendPartnerIncomeRequestBody(
    partnershipUtr = "4564564564",
    partnershipName = "ABC Partnership",
    startDate = Some("2026-06-24"),
    endDate = Some("2026-07-24"),
    cashBasis = Some(true),
    partnershipTrades = Some(
      Seq(
        PartnerShipTrade(
          tradeDescription = "Consultancy Services",
          tradingOrProfessionalProfits = Some(
            TradingOrProfessionalProfits(
              shareOfProfitOrLoss = 5000.99,
              basisAdjustment = Some(5000.99),
              accountingAdjustment = Some(5000.99),
              averagingAdjustment = Some(5000.99),
              foreignTaxClaimedDeduction = Some(5000.99),
              adjustedProfit = Some(5000.99),
              transitionProfitArisingThisYear = Some(5000.99),
              lossesBroughtForwardTransitionProfit = Some(5000.99),
              lossesBroughtForwardAdjustedProfit = Some(5000.99),
              profitAfterBroughtForwardLosses = Some(5000.99),
              otherBusinessIncome = Some(5000.99),
              shareOfTaxableProfit = 5000.99
            )
          ),
          tradingOrProfessionalLosses = Some(
            TradingOrProfessionalLosses(
              adjustedLoss = Some(5000.99),
              currentYearLossAppliedToGeneralIncome = Some(5000.99),
              lossesCarriedBack = Some(5000.99),
              carryForwardLosses = Some(5000.99)
            )
          )
        )
      )
    ),
    nationalInsuranceContributions = Some(
      NationalInsuranceContributions(
        voluntaryClass2Nics = Some(true),
        class4Exemption = Some(true),
        adjustmentToProfitsForClass4 = Some(5000.99)
      )
    ),
    untaxedSavingsIncome = Some(
      UntaxedSavingsIncome(
        shareOfUkUntaxedSavingsIncome = Some(5000.99),
        untaxedSavingsIncomeAdjustment = Some(5000.99),
        adjustedUkSavingsIncome = Some(5000.99),
        shareOfForeignUntaxedSavingsIncome = Some(5000.99),
        foreignUntaxedSavingsIncomeAdjustment = Some(5000.99),
        totalForeignTaxTakenOff = Some(5000.99),
        adjustedForeignSavingsIncome = Some(5000.99),
        totalUntaxedSavingsIncome = Some(5000.99)
      )
    ),
    ukPropertyIncome = Some(
      UkPropertyIncome(
        shareOfProfitOrLoss = Some(5000.99),
        profitOrLossAdjustment = Some(5000.99),
        lossesBroughtForward = Some(5000.99),
        currentYearLossAppliedToGeneralIncome = Some(5000.99),
        carryForwardLosses = Some(5000.99),
        shareOfTaxableProfit = Some(5000.99),
        residentialPropertyFinanceCosts = Some(5000.99),
        residentialPropertyFinanceCostsBroughtForward = Some(5000.99)
      )
    ),
    otherUntaxedUkIncome = Some(
      OtherUntaxedUkIncome(
        shareOfOtherUntaxedUkIncome = Some(5000.99),
        otherUntaxedUkIncomeAdjustment = Some(5000.99),
        lossesBroughtForward = Some(5000.99),
        shareOfTaxableProfit = Some(5000.99),
        shareOfLoss = Some(5000.99),
        otherUntaxedUkIncomeLossAdjustment = Some(5000.99),
        carryForwardLosses = Some(5000.99)
      )
    ),
    offshoreFundsIncome = Some(
      OffshoreFundsIncome(
        shareOfOffshoreFundsIncome = Some(5000.99),
        offshoreFundsIncomeAdjustment = Some(5000.99),
        totalForeignTaxTakenOff = Some(5000.99),
        shareOfTaxableIncome = Some(5000.99)
      )
    ),
    otherUntaxedForeignIncome = Some(
      OtherUntaxedForeignIncome(
        shareOfOtherUntaxedForeignIncome = Some(5000.99),
        otherUntaxedForeignIncomeAdjustment = Some(5000.99),
        lossesBroughtForward = Some(5000.99),
        totalForeignTaxTakenOff = Some(5000.99),
        shareOfTaxableProfit = Some(5000.99),
        shareOfLoss = Some(5000.99),
        otherUntaxedForeignIncomeLossesAdjustment = Some(5000.99),
        carryForwardLosses = Some(5000.99),
        residentialPropertyFinanceCosts = Some(5000.99),
        residentialPropertyFinanceCostsBroughtForward = Some(5000.99)
      )
    ),
    totalUntaxedIncomeExcludingSavings = Some(
      TotalUntaxedIncomeExcludingSavings(
        shareOfTotalUntaxedIncomeExcludingSavings = 5000.99
      )
    ),
    taxedIncomeAndDividendIncome = Some(
      TaxedIncomeAndDividendIncome(
        shareOfDividendIncome = Some(5000.99),
        dividendIncomeForeignTaxTakenOff = Some(5000.99),
        totalDividendIncome = Some(5000.99),
        shareOfTaxedIncome = Some(5000.99),
        taxedIncomeForeignTaxTakenOff = Some(5000.99),
        totalTaxedIncome = Some(5000.99),
        shareOfOtherTaxedIncome = Some(5000.99),
        otherTaxedIncomeForeignTaxTakenOff = Some(5000.99)
      )
    ),
    totalTaxedAndUntaxedIncome = Some(
      TotalTaxedAndUntaxedIncome(shareOfTotalTaxedAndUntaxedIncome = 5000.99)
    ),
    taxPaidAndDeductions = Some(
      TaxPaidAndDeductions(
        shareOfIncomeTaxTakenOffPartnershipIncome = Some(5000.99),
        shareOfCisDeductionsByContractors = Some(5000.99),
        shareOfTaxTakenOffTradingIncome = Some(5000.99),
        shareOfTotalTaxTakenOff = Some(5000.99)
      )
    )
  )

  "reads" should {
    "read from json" in {
      mtdJson.as[CreateAmendPartnerIncomeRequestBody] shouldBe model
    }
  }

  "writes" should {
    "write to json" in {
      Json.toJson(model) shouldBe downstreamJson
    }
  }

}
