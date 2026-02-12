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

case class OffshoreFundsIncome(
                                 shareOfOffshoreFundsIncome: Option[BigDecimal],
                                 offshoreFundsIncomeAdjustment: Option[BigDecimal],
                                 totalForeignTaxTakenOff: Option[BigDecimal],
                                 shareOfTaxableIncome: Option[BigDecimal]
)

object OffshoreFundsIncome {

  implicit val reads: Reads[OffshoreFundsIncome] = (
    (JsPath \ "shareOfOffshoreFundsIncome").readNullable[BigDecimal] and
      (JsPath \ "offShoreFundsIncomeAdjustment").readNullable[BigDecimal] and
      (JsPath \ "totalForeignTaxTakenOff").readNullable[BigDecimal] and
      (JsPath \ "shareOfTaxableIncome").readNullable[BigDecimal]
  )(OffshoreFundsIncome.apply)

  implicit val writes: OWrites[OffshoreFundsIncome] = Json.writes[OffshoreFundsIncome]
}
