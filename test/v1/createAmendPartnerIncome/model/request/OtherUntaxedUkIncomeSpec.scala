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

class OtherUntaxedUkIncomeSpec extends UnitSpec {

  private val mtdJson = Json.parse(
    """
      |{
      |    "shareOfOtherUntaxedUkIncome": 5000.99,
      |    "otherUntaxedUkIncomeAdjustment": 5000.99,
      |    "lossesBroughtForward": 5000.99,
      |    "shareOfTaxableProfit": 5000.99,
      |    "shareOfLoss": 5000.99,
      |    "otherUntaxedUkIncomeLossAdjustment": 5000.99,
      |    "carryForwardLosses": 5000.99
      |}
      |""".stripMargin
  )

  private val downstreamJson = Json.parse(
    """
      |{
      |    "shareOfOtherUntaxedUKIncome": 5000.99,
      |    "otherUntaxedUKIncomeAdjustment": 5000.99,
      |    "lossesBroughtForward": 5000.99,
      |    "shareOfTaxableProfit": 5000.99,
      |    "shareOfLoss": 5000.99,
      |    "otherUntaxedUKIncomeLossAdjustment": 5000.99,
      |    "carryForwardLosses": 5000.99
      |}
      |""".stripMargin
  )

  private val model = OtherUntaxedUkIncome(
    shareOfOtherUntaxedUkIncome = Some(5000.99),
    otherUntaxedUkIncomeAdjustment = Some(5000.99),
    lossesBroughtForward = Some(5000.99),
    shareOfTaxableProfit = Some(5000.99),
    shareOfLoss = Some(5000.99),
    otherUntaxedUkIncomeLossAdjustment = Some(5000.99),
    carryForwardLosses = Some(5000.99)
  )

  "reads" should {
    "read from json" in {
      mtdJson.as[OtherUntaxedUkIncome] shouldBe model
    }
  }

  "writes" should {
    "write to json" in {
      Json.toJson(model) shouldBe downstreamJson
    }
  }

}
