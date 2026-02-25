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

class UntaxedSavingsIncomeSpec extends UnitSpec {

  private val mtdJson = Json.parse(
    """
      |{
      |  "shareOfUkUntaxedSavingsIncome": 5000.99,
      |  "untaxedSavingsIncomeAdjustment": 5000.99,
      |  "adjustedUkSavingsIncome": 5000.99,
      |  "shareOfForeignUntaxedSavingsIncome": 5000.99,
      |  "foreignUntaxedSavingsIncomeAdjustment": 5000.99,
      |  "totalForeignTaxTakenOff": 5000.99,
      |  "adjustedForeignSavingsIncome": 5000.99,
      |  "totalUntaxedSavingsIncome": 5000.99
      |}
      |""".stripMargin
  )

  private val downstreamJson = Json.parse(
    """
      |{
      |  "shareOfUkUntaxedSavingsIncome": 5000.99,
      |  "untaxedSavingsIncomeAdjustment": 5000.99,
      |  "adjustedUKSavingsIncome": 5000.99,
      |  "shareOfForeignUntaxedSavingsIncome": 5000.99,
      |  "foreignUntaxedSavingsIncomeAdjustment": 5000.99,
      |  "totalForeignTaxTakenOff": 5000.99,
      |  "adjustedForeignSavingsIncome": 5000.99,
      |  "totalUntaxedSavingsIncome": 5000.99
      |}
      |""".stripMargin
  )

  private val model = UntaxedSavingsIncome(
    shareOfUkUntaxedSavingsIncome = Some(5000.99),
    untaxedSavingsIncomeAdjustment = Some(5000.99),
    adjustedUkSavingsIncome = Some(5000.99),
    shareOfForeignUntaxedSavingsIncome = Some(5000.99),
    foreignUntaxedSavingsIncomeAdjustment = Some(5000.99),
    totalForeignTaxTakenOff = Some(5000.99),
    adjustedForeignSavingsIncome = Some(5000.99),
    totalUntaxedSavingsIncome = Some(5000.99)
  )

  "reads" should {
    "read from json" in {
      mtdJson.as[UntaxedSavingsIncome] shouldBe model
    }
  }

  "writes" should {
    "write to json" in {
      Json.toJson(model) shouldBe downstreamJson
    }
  }

}
