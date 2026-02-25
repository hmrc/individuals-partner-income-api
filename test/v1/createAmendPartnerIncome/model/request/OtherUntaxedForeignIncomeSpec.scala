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

class OtherUntaxedForeignIncomeSpec extends UnitSpec {

  private val json = Json.parse(
    """
      |{
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
      |}
      |""".stripMargin
  )

  private val model = OtherUntaxedForeignIncome(
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

  "reads" should {
    "read from json" in {
      json.as[OtherUntaxedForeignIncome] shouldBe model
    }
  }

  "writes" should {
    "write to json" in {
      Json.toJson(model) shouldBe json
    }
  }

}
