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

case class UntaxedSavingsIncome(
    shareOfUkUntaxedSavingsIncome: Option[BigDecimal],
    untaxedSavingsIncomeAdjustment: Option[BigDecimal],
    adjustedUkSavingsIncome: Option[BigDecimal],
    shareOfForeignUntaxedSavingsIncome: Option[BigDecimal],
    foreignUntaxedSavingsIncomeAdjustment: Option[BigDecimal],
    totalForeignTaxTakenOff: Option[BigDecimal],
    adjustedForeignSavingsIncome: Option[BigDecimal],
    totalUntaxedSavingsIncome: Option[BigDecimal]
)

object UntaxedSavingsIncome {

  implicit val reads: Reads[UntaxedSavingsIncome] = (
    (JsPath \ "shareOfUkUntaxedSavingsIncome").readNullable[BigDecimal] and
      (JsPath \ "untaxedSavingsIncomeAdjustment").readNullable[BigDecimal] and
      (JsPath \ "adjustedUKSavingsIncome").readNullable[BigDecimal] and
      (JsPath \ "shareOfForeignUntaxedSavingsIncome").readNullable[BigDecimal] and
      (JsPath \ "foreignUntaxedSavingsIncomeAdjustment").readNullable[BigDecimal] and
      (JsPath \ "totalForeignTaxTakenOff").readNullable[BigDecimal] and
      (JsPath \ "adjustedForeignSavingsIncome").readNullable[BigDecimal] and
      (JsPath \ "totalUntaxedSavingsIncome").readNullable[BigDecimal]
  )(UntaxedSavingsIncome.apply)

  implicit val writes: OWrites[UntaxedSavingsIncome] = Json.writes[UntaxedSavingsIncome]
}
