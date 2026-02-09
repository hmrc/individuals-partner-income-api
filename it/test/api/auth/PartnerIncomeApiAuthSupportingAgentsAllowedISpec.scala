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

/* TODO: uncomment once create/amend endpoint has been created

package api.auth

import play.api.http.Status
import play.api.libs.json.{JsValue, Json}
import play.api.libs.ws.WSBodyWritables.writeableOf_JsValue
import play.api.libs.ws.{WSRequest, WSResponse}
import shared.auth.AuthSupportingAgentsAllowedISpec
import shared.services.DownstreamStub

class PartnerIncomeApiAuthSupportingAgentsAllowedISpec extends AuthSupportingAgentsAllowedISpec {

  val callingApiVersion = "1.0"

  val supportingAgentsAllowedEndpoint = "create-partner-income"

  val mtdUrl = s"/$nino/2026-27/partnership"

  def sendMtdRequest(request: WSRequest): WSResponse = await(request.put(requestJson()))

  val downstreamUri = s"/itsa/income-tax/v1/26-27/income/partnerships/$nino"

  override val downstreamHttpMethod: DownstreamStub.HTTPMethod = DownstreamStub.PUT

  val maybeDownstreamResponseJson: Option[JsValue] = None

  override protected val downstreamSuccessStatus: Int = Status.NO_CONTENT

  private def requestJson(): JsValue = {
    Json.parse(
      s"""
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
      """.stripMargin
    )
  }

}

 */
