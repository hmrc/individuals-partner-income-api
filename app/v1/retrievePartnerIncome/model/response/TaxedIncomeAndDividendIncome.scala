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

import play.api.libs.json.{Json, OFormat}

case class TaxedIncomeAndDividendIncome(
    shareOfDividendIncome: Option[BigDecimal],
    dividendIncomeForeignTaxTakenOff: Option[BigDecimal],
    totalDividendIncome: Option[BigDecimal],
    shareOfTaxedIncome: Option[BigDecimal],
    taxedIncomeForeignTaxTakenOff: Option[BigDecimal],
    totalTaxedIncome: Option[BigDecimal],
    shareOfOtherTaxedIncome: Option[BigDecimal],
    otherTaxedIncomeForeignTaxTakenOff: Option[BigDecimal]
)

object TaxedIncomeAndDividendIncome {
  implicit val format: OFormat[TaxedIncomeAndDividendIncome] = Json.format[TaxedIncomeAndDividendIncome]
}
