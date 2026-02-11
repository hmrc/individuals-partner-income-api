package v1.list.model.response

import api.utils.UnitSpec
import play.api.libs.json.{JsValue, Json}
import v1.list.ListPartnerIncomeFixtures.*

class ListPartnerIncomeResponseSpec extends UnitSpec {

  "Json Reads" should {
    "convert Json from downstream into a valid response model" in {

      downstreamJson.as[ListPartnerIncomeResponse] shouldBe responseModel
    }
  }

  "Json Writes" should {
    "convert a ListPartnerIncomeResponse model to json" in {

      Json.toJson(responseModel) shouldBe mtdJson
    }
  }
}
