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

import api.models.domain.{Nino, PartnershipUtr, TaxYear, Timestamp}
import play.api.libs.json.{JsValue, Json}
import v1.retrievePartnerIncome.model.request.RetrievePartnerIncomeRequestData
import v1.retrievePartnerIncome.model.response.*

object RetrievePartnerIncomeFixtures {

  val nino: String           = "AA123456A"
  val partnershipUtr: String = "1234567890"
  val taxYear: String        = "2026-27"

  val request: RetrievePartnerIncomeRequestData =
    RetrievePartnerIncomeRequestData(Nino(nino), TaxYear.fromMtd(taxYear), PartnershipUtr(partnershipUtr))

  val responseModel: RetrievePartnerIncomeResponse =
    RetrievePartnerIncomeResponse(
      submittedOn = Timestamp("2026-08-24T14:15:22.544Z"),
      partnershipUtr = "4564564564",
      partnershipName = "ABC Partnership",
      startDate = Some("2026-06-24"),
      endDate = Some("2026-07-24"),
      cashBasis = Some(true),
      partnershipTrades = Some(
        Seq(PartnershipTrade(
          tradeDescription = "Consultancy Services",
          tradingOrProfessionalProfits = Some(TradingOrProfessionalProfits(
            shareOfProfitOrLoss = 99999999999.99,
            basisAdjustment = Some(99999999999.99),
            accountingAdjustment = Some(5000.99),
            averagingAdjustment = Some(99999999999.99),
            foreignTaxClaimedDeduction = Some(5000.99),
            adjustedProfit = 5000.99,
            transitionProfitArisingThisYear = Some(5000.99),
            lossesBroughtForwardTransitionProfit = Some(5000.99),
            lossesBroughtForwardAdjustedProfit = Some(5000.99),
            profitAfterBroughtForwardLosses = Some(5000.99),
            otherBusinessIncome = Some(5000.99),
            shareOfTaxableProfit = 5000.99
          )),
          tradingOrProfessionalLosses = Some(
            TradingOrProfessionalLosses(
              adjustedLoss = Some(5000.99),
              currentYearLossAppliedToGeneralIncome = Some(5000.99),
              lossesCarriedBack = Some(5000.99),
              carryForwardLosses = Some(5000.99)
            ))
        ))),
      nationalInsuranceContributions = Some(
        NationalInsuranceContributions(
          voluntaryClass2Nics = Some(true),
          class4Exemption = Some(true),
          adjustmentToProfitsForClass4 = Some(5000.99)
        )),
      untaxedSavingsIncome = Some(
        UntaxedSavingsIncome(
          shareOfUkUntaxedSavingsIncome = Some(5000.99),
          untaxedSavingsIncomeAdjustment = Some(99999999999.99),
          adjustedUkSavingsIncome = Some(5000.99),
          shareOfForeignUntaxedSavingsIncome = Some(5000.99),
          foreignUntaxedSavingsIncomeAdjustment = Some(99999999999.99),
          totalForeignTaxTakenOff = Some(5000.99),
          adjustedForeignSavingsIncome = Some(5000.99),
          totalUntaxedSavingsIncome = Some(5000.99)
        )),
      ukPropertyIncome = Some(
        UkPropertyIncome(
          shareOfProfitOrLoss = Some(99999999999.99),
          profitOrLossAdjustment = Some(99999999999.99),
          lossesBroughtForward = Some(5000.99),
          currentYearLossAppliedToGeneralIncome = Some(5000.99),
          carryForwardLosses = Some(5000.99),
          shareOfTaxableProfit = Some(5000.99),
          residentialPropertyFinanceCosts = Some(5000.99),
          residentialPropertyFinanceCostsBroughtForward = Some(5000.99)
        )),
      otherUntaxedUkIncome = Some(
        OtherUntaxedUkIncome(
          shareOfOtherUntaxedUkIncome = Some(5000.99),
          otherUntaxedUkIncomeAdjustment = Some(99999999999.99),
          lossesBroughtForward = Some(5000.99),
          shareOfTaxableProfit = Some(5000.99),
          shareOfLoss = Some(5000.99),
          otherUntaxedUkIncomeLossAdjustment = Some(99999999999.99),
          carryForwardLosses = Some(5000.99)
        )),
      offshoreFundsIncome = Some(
        OffshoreFundsIncome(
          shareOfOffshoreFundsIncome = Some(5000.99),
          offshoreFundsIncomeAdjustment = Some(99999999999.99),
          totalForeignTaxTakenOff = Some(5000.99),
          shareOfTaxableIncome = Some(5000.99)
        )),
      otherUntaxedForeignIncome = Some(
        OtherUntaxedForeignIncome(
          shareOfOtherUntaxedForeignIncome = Some(5000.99),
          otherUntaxedForeignIncomeAdjustment = Some(99999999999.99),
          lossesBroughtForward = Some(5000.99),
          totalForeignTaxTakenOff = Some(5000.99),
          shareOfTaxableProfit = Some(5000.99),
          shareOfLoss = Some(5000.99),
          otherUntaxedForeignIncomeLossesAdjustment = Some(99999999999.99),
          carryForwardLosses = Some(5000.99),
          residentialPropertyFinanceCosts = Some(5000.99),
          residentialPropertyFinanceCostsBroughtForward = Some(5000.99)
        )),
      totalUntaxedIncomeExcludingSavings = Some(
        TotalUntaxedIncomeExcludingSavings(
          shareOfTotalUntaxedIncomeExcludingSavings = 5000.99
        )),
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
        )),
      totalTaxedAndUntaxedIncome = Some(
        TotalTaxedAndUntaxedIncome(
          shareOfTotalTaxedAndUntaxedIncome = 5000.99
        )),
      taxPaidAndDeductions = Some(
        TaxPaidAndDeductions(
          shareOfIncomeTaxTakenOffPartnershipIncome = Some(5000.99),
          shareOfCisDeductionsByContractors = Some(5000.99),
          shareOfTaxTakenOffTradingIncome = Some(5000.99),
          shareOfTotalTaxTakenOff = Some(5000.99)
        ))
    )

  val responseModelWithMissingFields: RetrievePartnerIncomeResponse =
    RetrievePartnerIncomeResponse(
      submittedOn = Timestamp("2026-08-24T14:15:22.544Z"),
      partnershipUtr = "4564564564",
      partnershipName = "ABC Partnership",
      startDate = None,
      endDate = None,
      cashBasis = None,
      partnershipTrades = Some(
        Seq(PartnershipTrade(
          tradeDescription = "Consultancy Services",
          tradingOrProfessionalProfits = Some(TradingOrProfessionalProfits(
            shareOfProfitOrLoss = 99999999999.99,
            basisAdjustment = Some(99999999999.99),
            accountingAdjustment = Some(5000.99),
            averagingAdjustment = Some(99999999999.99),
            foreignTaxClaimedDeduction = Some(5000.99),
            adjustedProfit = 5000.99,
            transitionProfitArisingThisYear = Some(5000.99),
            lossesBroughtForwardTransitionProfit = Some(5000.99),
            lossesBroughtForwardAdjustedProfit = Some(5000.99),
            profitAfterBroughtForwardLosses = Some(5000.99),
            otherBusinessIncome = None,
            shareOfTaxableProfit = 5000.99
          )),
          tradingOrProfessionalLosses = None
        ))),
      nationalInsuranceContributions = Some(
        NationalInsuranceContributions(
          voluntaryClass2Nics = Some(false),
          class4Exemption = None,
          adjustmentToProfitsForClass4 = None
        )),
      untaxedSavingsIncome = None,
      ukPropertyIncome = Some(
        UkPropertyIncome(
          shareOfProfitOrLoss = Some(99999999999.99),
          profitOrLossAdjustment = Some(99999999999.99),
          lossesBroughtForward = Some(5000.99),
          currentYearLossAppliedToGeneralIncome = Some(5000.99),
          carryForwardLosses = Some(5000.99),
          shareOfTaxableProfit = Some(5000.99),
          residentialPropertyFinanceCosts = Some(5000.99),
          residentialPropertyFinanceCostsBroughtForward = Some(5000.99)
        )),
      otherUntaxedUkIncome = None,
      offshoreFundsIncome = None,
      otherUntaxedForeignIncome = None,
      totalUntaxedIncomeExcludingSavings = None,
      taxedIncomeAndDividendIncome = Some(
        TaxedIncomeAndDividendIncome(
          shareOfDividendIncome = Some(5000.99),
          dividendIncomeForeignTaxTakenOff = Some(5000.99),
          totalDividendIncome = Some(5000.99),
          shareOfTaxedIncome = Some(5000.99),
          taxedIncomeForeignTaxTakenOff = Some(5000.99),
          totalTaxedIncome = Some(5000.99),
          shareOfOtherTaxedIncome = None,
          otherTaxedIncomeForeignTaxTakenOff = None
        )),
      totalTaxedAndUntaxedIncome = Some(
        TotalTaxedAndUntaxedIncome(
          shareOfTotalTaxedAndUntaxedIncome = 5000.99
        )),
      taxPaidAndDeductions = Some(
        TaxPaidAndDeductions(
          shareOfIncomeTaxTakenOffPartnershipIncome = None,
          shareOfCisDeductionsByContractors = None,
          shareOfTaxTakenOffTradingIncome = Some(5000.99),
          shareOfTotalTaxTakenOff = Some(5000.99)
        ))
    )

  val downstreamJson: JsValue = Json.parse("""
       |[
       |  {
       |    "submittedOn": "2026-08-24T14:15:22.544Z",
       |    "partnershipUTR": "4564564564",
       |    "partnershipName": "ABC Partnership",
       |    "startDate": "2026-06-24",
       |    "endDate": "2026-07-24",
       |    "cashBasis": true,
       |    "partnershipTrades": [
       |      {
       |        "tradeDescription": "Consultancy Services",
       |        "tradingOrProfessionalProfits": {
       |          "shareOfProfitOrLoss": 99999999999.99,
       |          "basisAdjustment": 99999999999.99,
       |          "accountingAdjustment": 5000.99,
       |          "averagingAdjustment": 99999999999.99,
       |          "foreignTaxClaimedDeduction": 5000.99,
       |          "adjustedProfit": 5000.99,
       |          "transitionProfitArisingThisYear": 5000.99,
       |          "lossesBroughtForwardTransitionProfit": 5000.99,
       |          "lossesBroughtForwardAdjustedProfit": 5000.99,
       |          "profitAfterBroughtForwardLosses": 5000.99,
       |          "otherBusinessIncome": 5000.99,
       |          "shareOfTaxableProfit": 5000.99
       |        },
       |        "tradingOrProfessionalLosses": {
       |          "adjustedLoss": 5000.99,
       |          "currentYearLossAppliedToGeneralIncome": 5000.99,
       |          "lossesCarriedBack": 5000.99,
       |          "carryForwardLosses": 5000.99
       |        }
       |      }
       |    ],
       |    "nationalInsuranceContributions": {
       |      "voluntaryClass2Nics": true,
       |      "class4Exemption": true,
       |      "adjustmentToProfitsForClass4": 5000.99
       |    },
       |    "untaxedSavingsIncome": {
       |      "shareOfUkUntaxedSavingsIncome": 5000.99,
       |      "untaxedSavingsIncomeAdjustment": 99999999999.99,
       |      "adjustedUKSavingsIncome": 5000.99,
       |      "shareOfForeignUntaxedSavingsIncome": 5000.99,
       |      "foreignUntaxedSavingsIncomeAdjustment": 99999999999.99,
       |      "totalForeignTaxTakenOff": 5000.99,
       |      "adjustedForeignSavingsIncome": 5000.99,
       |      "totalUntaxedSavingsIncome": 5000.99
       |    },
       |    "ukPropertyIncome": {
       |      "shareOfProfitOrLoss": 99999999999.99,
       |      "profitOrLossAdjustment": 99999999999.99,
       |      "lossesBroughtForward": 5000.99,
       |      "currentYearLossAppliedToGeneralIncome": 5000.99,
       |      "carryForwardLosses": 5000.99,
       |      "shareOfTaxableProfit": 5000.99,
       |      "residentialPropertyFinanceCosts": 5000.99,
       |      "residentialPropertyFinanceCostsBroughtForward": 5000.99
       |    },
       |    "otherUntaxedUKIncome": {
       |      "shareOfOtherUntaxedUKIncome": 5000.99,
       |      "otherUntaxedUKIncomeAdjustment": 99999999999.99,
       |      "lossesBroughtForward": 5000.99,
       |      "shareOfTaxableProfit": 5000.99,
       |      "shareOfLoss": 5000.99,
       |      "otherUntaxedUKIncomeLossAdjustment": 99999999999.99,
       |      "carryForwardLosses": 5000.99
       |    },
       |    "offshoreFundsIncome": {
       |      "shareOfOffshoreFundsIncome": 5000.99,
       |      "offShoreFundsIncomeAdjustment": 99999999999.99,
       |      "totalForeignTaxTakenOff": 5000.99,
       |      "shareOfTaxableIncome": 5000.99
       |    },
       |    "otherUntaxedForeignIncome": {
       |      "shareOfOtherUntaxedForeignIncome": 5000.99,
       |      "otherUntaxedForeignIncomeAdjustment": 99999999999.99,
       |      "lossesBroughtForward": 5000.99,
       |      "totalForeignTaxTakenOff": 5000.99,
       |      "shareOfTaxableProfit": 5000.99,
       |      "shareOfLoss": 5000.99,
       |      "otherUntaxedForeignIncomeLossesAdjustment": 99999999999.99,
       |      "carryForwardLosses": 5000.99,
       |      "residentialPropertyFinanceCosts": 5000.99,
       |      "residentialPropertyFinanceCostsBroughtForward": 5000.99
       |    },
       |    "totalUntaxedIncomeExcludingSavings": {
       |      "shareOfTotalUntaxedIncomeExcludingSavings": 5000.99
       |    },
       |    "taxedIncomeAndDividendIncome": {
       |      "shareOfDividendIncome": 5000.99,
       |      "dividendIncomeForeignTaxTakenOff": 5000.99,
       |      "totalDividendIncome": 5000.99,
       |      "shareOfTaxedIncome": 5000.99,
       |      "taxedIncomeForeignTaxTakenOff": 5000.99,
       |      "totalTaxedIncome": 5000.99,
       |      "shareOfOtherTaxedIncome": 5000.99,
       |      "otherTaxedIncomeForeignTaxTakenOff": 5000.99
       |    },
       |    "totalTaxedAndUntaxedIncome": {
       |      "shareOfTotalTaxedAndUntaxedIncome": 5000.99
       |    },
       |    "taxPaidAndDeductions": {
       |      "shareOfIncomeTaxTakenOffPartnershipIncome": 5000.99,
       |      "shareOfCisDeductionsByContractors": 5000.99,
       |      "shareOfTaxTakenOffTradingIncome": 5000.99,
       |      "shareOfTotalTaxTakenOff": 5000.99
       |    }
       |  }
       |]
       |""".stripMargin)

  val downstreamJsonWithMissingFields: JsValue = Json.parse("""
       |[
       |  {
       |    "submittedOn": "2026-08-24T14:15:22.544Z",
       |    "partnershipUTR": "4564564564",
       |    "partnershipName": "ABC Partnership",
       |    "partnershipTrades": [
       |      {
       |        "tradeDescription": "Consultancy Services",
       |        "tradingOrProfessionalProfits": {
       |          "shareOfProfitOrLoss": 99999999999.99,
       |          "basisAdjustment": 99999999999.99,
       |          "accountingAdjustment": 5000.99,
       |          "averagingAdjustment": 99999999999.99,
       |          "foreignTaxClaimedDeduction": 5000.99,
       |          "adjustedProfit": 5000.99,
       |          "transitionProfitArisingThisYear": 5000.99,
       |          "lossesBroughtForwardTransitionProfit": 5000.99,
       |          "lossesBroughtForwardAdjustedProfit": 5000.99,
       |          "profitAfterBroughtForwardLosses": 5000.99,
       |          "shareOfTaxableProfit": 5000.99
       |        }
       |      }
       |    ],
       |    "nationalInsuranceContributions": {
       |      "voluntaryClass2Nics": false
       |    },
       |    "ukPropertyIncome": {
       |      "shareOfProfitOrLoss": 99999999999.99,
       |      "profitOrLossAdjustment": 99999999999.99,
       |      "lossesBroughtForward": 5000.99,
       |      "currentYearLossAppliedToGeneralIncome": 5000.99,
       |      "carryForwardLosses": 5000.99,
       |      "shareOfTaxableProfit": 5000.99,
       |      "residentialPropertyFinanceCosts": 5000.99,
       |      "residentialPropertyFinanceCostsBroughtForward": 5000.99
       |    },
       |    "taxedIncomeAndDividendIncome": {
       |      "shareOfDividendIncome": 5000.99,
       |      "dividendIncomeForeignTaxTakenOff": 5000.99,
       |      "totalDividendIncome": 5000.99,
       |      "shareOfTaxedIncome": 5000.99,
       |      "taxedIncomeForeignTaxTakenOff": 5000.99,
       |      "totalTaxedIncome": 5000.99
       |    },
       |    "totalTaxedAndUntaxedIncome": {
       |      "shareOfTotalTaxedAndUntaxedIncome": 5000.99
       |    },
       |    "taxPaidAndDeductions": {
       |      "shareOfTaxTakenOffTradingIncome": 5000.99,
       |      "shareOfTotalTaxTakenOff": 5000.99
       |    }
       |  }
       |]
       |""".stripMargin)

  val downstreamJsonInvalidFields: JsValue = Json.parse("""
       |[
       |  {
       |    "submittedOn": "2026-08-24T14:15:22.544Z",
       |    "partnershipUTR": "4564564564",
       |    "partnershipTrades": [
       |      {
       |        "tradeDescription": "Consultancy Services"
       |      }
       |    ]
       |  }
       |]
       |""".stripMargin)

  val downstreamJsonNotInArray: JsValue = Json.parse("""
       |{
       |  "submittedOn": "2026-08-24T14:15:22.544Z",
       |  "partnershipUTR": "4564564564",
       |  "partnershipName": "ABC Partnership",
       |  "partnershipTrades": [
       |    {
       |      "tradeDescription": "Consultancy Services"
       |    }
       |  ]
       |}
       |""".stripMargin)

  val downstreamJsonMultipleObjectsInArray: JsValue = Json.parse("""
      |[
      |  {
      |    "submittedOn": "2026-08-24T14:15:22.544Z",
      |    "partnershipUTR": "4564564564",
      |    "partnershipName": "ABC Partnership",
      |    "partnershipTrades": [
      |      {
      |        "tradeDescription": "Consultancy Services"
      |      }
      |    ]
      |  },
      |  {
      |    "submittedOn": "2026-08-25T14:15:22.544Z",
      |    "partnershipUTR": "1234567890",
      |    "partnershipName": "DEF Partnership",
      |    "partnershipTrades": [
      |      {
      |        "tradeDescription": "Consultancy Services"
      |      }
      |    ]
      |  }
      |]
      |""".stripMargin)

  val mtdJson: JsValue = Json.parse("""
       |{
       |  "submittedOn": "2026-08-24T14:15:22.544Z",
       |  "partnershipUtr": "4564564564",
       |  "partnershipName": "ABC Partnership",
       |  "startDate": "2026-06-24",
       |  "endDate": "2026-07-24",
       |  "cashBasis": true,
       |  "partnershipTrades": [
       |    {
       |      "tradeDescription": "Consultancy Services",
       |      "tradingOrProfessionalProfits": {
       |        "shareOfProfitOrLoss": 99999999999.99,
       |        "basisAdjustment": 99999999999.99,
       |        "accountingAdjustment": 5000.99,
       |        "averagingAdjustment": 99999999999.99,
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
       |    "untaxedSavingsIncomeAdjustment": 99999999999.99,
       |    "adjustedUkSavingsIncome": 5000.99,
       |    "shareOfForeignUntaxedSavingsIncome": 5000.99,
       |    "foreignUntaxedSavingsIncomeAdjustment": 99999999999.99,
       |    "totalForeignTaxTakenOff": 5000.99,
       |    "adjustedForeignSavingsIncome": 5000.99,
       |    "totalUntaxedSavingsIncome": 5000.99
       |  },
       |  "ukPropertyIncome": {
       |    "shareOfProfitOrLoss": 99999999999.99,
       |    "profitOrLossAdjustment": 99999999999.99,
       |    "lossesBroughtForward": 5000.99,
       |    "currentYearLossAppliedToGeneralIncome": 5000.99,
       |    "carryForwardLosses": 5000.99,
       |    "shareOfTaxableProfit": 5000.99,
       |    "residentialPropertyFinanceCosts": 5000.99,
       |    "residentialPropertyFinanceCostsBroughtForward": 5000.99
       |  },
       |  "otherUntaxedUkIncome": {
       |    "shareOfOtherUntaxedUkIncome": 5000.99,
       |    "otherUntaxedUkIncomeAdjustment": 99999999999.99,
       |    "lossesBroughtForward": 5000.99,
       |    "shareOfTaxableProfit": 5000.99,
       |    "shareOfLoss": 5000.99,
       |    "otherUntaxedUkIncomeLossAdjustment": 99999999999.99,
       |    "carryForwardLosses": 5000.99
       |  },
       |  "offshoreFundsIncome": {
       |    "shareOfOffshoreFundsIncome": 5000.99,
       |    "offshoreFundsIncomeAdjustment": 99999999999.99,
       |    "totalForeignTaxTakenOff": 5000.99,
       |    "shareOfTaxableIncome": 5000.99
       |  },
       |  "otherUntaxedForeignIncome": {
       |    "shareOfOtherUntaxedForeignIncome": 5000.99,
       |    "otherUntaxedForeignIncomeAdjustment": 99999999999.99,
       |    "lossesBroughtForward": 5000.99,
       |    "totalForeignTaxTakenOff": 5000.99,
       |    "shareOfTaxableProfit": 5000.99,
       |    "shareOfLoss": 5000.99,
       |    "otherUntaxedForeignIncomeLossesAdjustment": 99999999999.99,
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
       |""".stripMargin)

  val mtdJsonWithMissingFields: JsValue = Json.parse("""
       |{
       |  "submittedOn": "2026-08-24T14:15:22.544Z",
       |  "partnershipUtr": "4564564564",
       |  "partnershipName": "ABC Partnership",
       |  "partnershipTrades": [
       |    {
       |      "tradeDescription": "Consultancy Services",
       |      "tradingOrProfessionalProfits": {
       |        "shareOfProfitOrLoss": 99999999999.99,
       |        "basisAdjustment": 99999999999.99,
       |        "accountingAdjustment": 5000.99,
       |        "averagingAdjustment": 99999999999.99,
       |        "foreignTaxClaimedDeduction": 5000.99,
       |        "adjustedProfit": 5000.99,
       |        "transitionProfitArisingThisYear": 5000.99,
       |        "lossesBroughtForwardTransitionProfit": 5000.99,
       |        "lossesBroughtForwardAdjustedProfit": 5000.99,
       |        "profitAfterBroughtForwardLosses": 5000.99,
       |        "shareOfTaxableProfit": 5000.99
       |      }
       |    }
       |  ],
       |  "nationalInsuranceContributions": {
       |    "voluntaryClass2Nics": false
       |  },
       |  "ukPropertyIncome": {
       |    "shareOfProfitOrLoss": 99999999999.99,
       |    "profitOrLossAdjustment": 99999999999.99,
       |    "lossesBroughtForward": 5000.99,
       |    "currentYearLossAppliedToGeneralIncome": 5000.99,
       |    "carryForwardLosses": 5000.99,
       |    "shareOfTaxableProfit": 5000.99,
       |    "residentialPropertyFinanceCosts": 5000.99,
       |    "residentialPropertyFinanceCostsBroughtForward": 5000.99
       |  },
       |  "taxedIncomeAndDividendIncome": {
       |    "shareOfDividendIncome": 5000.99,
       |    "dividendIncomeForeignTaxTakenOff": 5000.99,
       |    "totalDividendIncome": 5000.99,
       |    "shareOfTaxedIncome": 5000.99,
       |    "taxedIncomeForeignTaxTakenOff": 5000.99,
       |    "totalTaxedIncome": 5000.99
       |  },
       |  "totalTaxedAndUntaxedIncome": {
       |    "shareOfTotalTaxedAndUntaxedIncome": 5000.99
       |  },
       |  "taxPaidAndDeductions": {
       |    "shareOfTaxTakenOffTradingIncome": 5000.99,
       |    "shareOfTotalTaxTakenOff": 5000.99
       |  }
       |}
       |""".stripMargin)

}
