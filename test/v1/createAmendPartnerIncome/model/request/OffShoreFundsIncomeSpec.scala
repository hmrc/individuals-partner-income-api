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

class OffShoreFundsIncomeSpec extends UnitSpec {

  private val mtdJson = Json.parse(
    """
      |{
      |    "shareOfOffshoreFundsIncome": 5000.99,
      |    "offshoreFundsIncomeAdjustment": 5000.99,
      |    "totalForeignTaxTakenOff": 5000.99,
      |    "shareOfTaxableIncome": 5000.99
      |}
      |""".stripMargin
  )

  private val downstreamJson = Json.parse(
    """
      |{
      |    "shareOfOffshoreFundsIncome": 5000.99,
      |    "offShoreFundsIncomeAdjustment": 5000.99,
      |    "totalForeignTaxTakenOff": 5000.99,
      |    "shareOfTaxableIncome": 5000.99
      |}
      |""".stripMargin
  )

  private val model = OffshoreFundsIncome(
    shareOfOffshoreFundsIncome = Some(5000.99),
    offshoreFundsIncomeAdjustment = Some(5000.99),
    totalForeignTaxTakenOff = Some(5000.99),
    shareOfTaxableIncome = Some(5000.99)
  )

  "reads" should {
    "read from json" in {
      mtdJson.as[OffshoreFundsIncome] shouldBe model
    }
  }

  "writes" should {
    "write to json" in {
      Json.toJson(model) shouldBe downstreamJson
    }
  }

}
