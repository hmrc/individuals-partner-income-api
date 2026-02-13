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

package v1.retrievePartnerIncome.model.response

import play.api.libs.functional.syntax.*
import play.api.libs.json.{JsPath, Json, OWrites, Reads}

case class OtherUntaxedUkIncome(
    shareOfOtherUntaxedUkIncome: Option[BigDecimal],
    otherUntaxedUkIncomeAdjustment: Option[BigDecimal],
    lossesBroughtForward: Option[BigDecimal],
    shareOfTaxableProfit: Option[BigDecimal],
    shareOfLoss: Option[BigDecimal],
    otherUntaxedUkIncomeLossAdjustment: Option[BigDecimal],
    carryForwardLosses: Option[BigDecimal]
)

object OtherUntaxedUkIncome {

  implicit val reads: Reads[OtherUntaxedUkIncome] = (
    (JsPath \ "shareOfOtherUntaxedUKIncome").readNullable[BigDecimal] and
      (JsPath \ "otherUntaxedUKIncomeAdjustment").readNullable[BigDecimal] and
      (JsPath \ "lossesBroughtForward").readNullable[BigDecimal] and
      (JsPath \ "shareOfTaxableProfit").readNullable[BigDecimal] and
      (JsPath \ "shareOfLoss").readNullable[BigDecimal] and
      (JsPath \ "otherUntaxedUKIncomeLossAdjustment").readNullable[BigDecimal] and
      (JsPath \ "carryForwardLosses").readNullable[BigDecimal]
  )(OtherUntaxedUkIncome.apply)

  implicit val writes: OWrites[OtherUntaxedUkIncome] = Json.writes[OtherUntaxedUkIncome]
}
