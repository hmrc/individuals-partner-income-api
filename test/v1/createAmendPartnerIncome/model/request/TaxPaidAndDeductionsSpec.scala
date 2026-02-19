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

class TaxPaidAndDeductionsSpec extends UnitSpec {

  private val json = Json.parse(
    """
      |{
      |  "shareOfIncomeTaxTakenOffPartnershipIncome": 5000.99,
      |  "shareOfCisDeductionsByContractors": 5000.99,
      |  "shareOfTaxTakenOffTradingIncome": 5000.99,
      |  "shareOfTotalTaxTakenOff": 5000.99
      |}
      |""".stripMargin
  )

  private val model = TaxPaidAndDeductions(
    shareOfIncomeTaxTakenOffPartnershipIncome = Some(5000.99),
    shareOfCisDeductionsByContractors = Some(5000.99),
    shareOfTaxTakenOffTradingIncome = Some(5000.99),
    shareOfTotalTaxTakenOff = Some(5000.99)
  )

  "reads" should {
    "read from json" in {
      json.as[TaxPaidAndDeductions] shouldBe model
    }
  }

  "writes" should {
    "write to json" in {
      Json.toJson(model) shouldBe json
    }
  }

}
