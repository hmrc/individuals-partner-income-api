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

import api.utils.UnitSpec
import play.api.libs.json.{JsError, JsValue, Json}
import v1.retrievePartnerIncome.RetrievePartnerIncomeFixtures.*

class RetrievePartnerIncomeResponseSpec extends UnitSpec {

  "Json Reads" should {
    "convert Json from downstream into a valid response model" in {
      downstreamJson.as[RetrievePartnerIncomeResponse] shouldBe responseModel
    }

    "convert Json from downstream into a valid response model when some optional fields are missing" in {
      downstreamJsonWithMissingFields.as[RetrievePartnerIncomeResponse] shouldBe responseModelWithMissingFields
    }

    "produce a JsError given a downstream Json with invalid fields" in {
      downstreamJsonInvalidFields.validate[RetrievePartnerIncomeResponse] shouldBe a[JsError]
    }

    "produce a JsError if the downstream Json is not an array containing the response object" in {
      downstreamJsonNotInArray.validate[RetrievePartnerIncomeResponse] shouldBe a[JsError]
    }

    "produce a JsError if the downstream Json is an array containing more than one item" in {
      downstreamJsonMultipleObjectsInArray.validate[RetrievePartnerIncomeResponse] shouldBe a[JsError]
    }
  }

  "Json Writes" should {
    "convert a ListPartnerIncomeResponse model to json" in {
      Json.toJson(responseModel) shouldBe mtdJson
    }

    "convert a ListPartnerIncomeResponse model to json when some optional fields are missing" in {
      Json.toJson(responseModelWithMissingFields) shouldBe mtdJsonWithMissingFields
    }
  }

}
