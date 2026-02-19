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

import play.api.libs.functional.syntax.toFunctionalBuilderOps
import play.api.libs.json.*

case class CreateAmendPartnerIncomeRequestBody(
    partnershipUtr: String,
    partnershipName: String,
    startDate: Option[String],
    endDate: Option[String],
    cashBasis: Option[Boolean],
    partnershipTrades: Option[Seq[PartnerShipTrade]],
    nationalInsuranceContributions: Option[NationalInsuranceContributions],
    untaxedSavingsIncome: Option[UntaxedSavingsIncome],
    ukPropertyIncome: Option[UkPropertyIncome],
    otherUntaxedUkIncome: Option[OtherUntaxedUkIncome],
    offshoreFundsIncome: Option[OffshoreFundsIncome],
    otherUntaxedForeignIncome: Option[OtherUntaxedForeignIncome],
    totalUntaxedIncomeExcludingSavings: Option[TotalUntaxedIncomeExcludingSavings],
    taxedIncomeAndDividendIncome: Option[TaxedIncomeAndDividendIncome],
    totalTaxedAndUntaxedIncome: Option[TotalTaxedAndUntaxedIncome],
    taxPaidAndDeductions: Option[TaxPaidAndDeductions]
)

object CreateAmendPartnerIncomeRequestBody {
  given Reads[CreateAmendPartnerIncomeRequestBody] = Json.reads[CreateAmendPartnerIncomeRequestBody]

  given Writes[CreateAmendPartnerIncomeRequestBody] = (
    (JsPath \ "partnershipUTR").write[String] and
      (JsPath \ "partnershipName").write[String] and
      (JsPath \ "startDate").writeNullable[String] and
      (JsPath \ "endDate").writeNullable[String] and
      (JsPath \ "cashBasis").writeNullable[Boolean] and
      (JsPath \ "partnershipTrades").writeNullable[Seq[PartnerShipTrade]] and
      (JsPath \ "nationalInsuranceContributions").writeNullable[NationalInsuranceContributions] and
      (JsPath \ "untaxedSavingsIncome").writeNullable[UntaxedSavingsIncome] and
      (JsPath \ "ukPropertyIncome").writeNullable[UkPropertyIncome] and
      (JsPath \ "otherUntaxedUKIncome").writeNullable[OtherUntaxedUkIncome] and
      (JsPath \ "offshoreFundsIncome").writeNullable[OffshoreFundsIncome] and
      (JsPath \ "otherUntaxedForeignIncome").writeNullable[OtherUntaxedForeignIncome] and
      (JsPath \ "totalUntaxedIncomeExcludingSavings").writeNullable[TotalUntaxedIncomeExcludingSavings] and
      (JsPath \ "taxedIncomeAndDividendIncome").writeNullable[TaxedIncomeAndDividendIncome] and
      (JsPath \ "totalTaxedAndUntaxedIncome").writeNullable[TotalTaxedAndUntaxedIncome] and
      (JsPath \ "taxPaidAndDeductions").writeNullable[TaxPaidAndDeductions]
  )(o => Tuple.fromProductTyped(o))

}
