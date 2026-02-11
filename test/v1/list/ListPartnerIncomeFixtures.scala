package v1.list

import api.models.domain.{Nino, TaxYear}
import play.api.libs.json.{JsValue, Json}
import v1.list.model.request.ListPartnerIncomeRequestData
import v1.list.model.response.{ListPartnerIncomeResponse, PartnerIncomeSubmission}

object ListPartnerIncomeFixtures {

  val taxYear: TaxYear                      = TaxYear.fromMtd("2026-27")
  val nino: Nino                            = Nino("AA123456A")
  val request: ListPartnerIncomeRequestData = ListPartnerIncomeRequestData(nino, taxYear)

  val responseModel: ListPartnerIncomeResponse =
    ListPartnerIncomeResponse(
      Seq(
        PartnerIncomeSubmission("4564564564", "ABC Partnership"),
        PartnerIncomeSubmission("7897897897", "DEF Partnership")
      )
    )

  val downstreamJson: JsValue = Json.parse(
    """
      |{
      |  "partnerIncomeSubmissions": [
      |    {
      |      "partnershipUTR": "4564564564",
      |      "partnershipName": "ABC Partnership"
      |    },
      |    {
      |      "partnershipUTR": "7897897897",
      |      "partnershipName": "DEF Partnership"
      |    }
      |  ]
      |}
      |""".stripMargin
  )

  val mtdJson: JsValue = Json.parse(
    """
      |{
      |  "partnerIncomeSubmissions": [
      |    {
      |      "partnershipUtr": "4564564564",
      |      "partnershipName": "ABC Partnership"
      |    },
      |    {
      |      "partnershipUtr": "7897897897",
      |      "partnershipName": "DEF Partnership"
      |    }
      |  ]
      |}
      |""".stripMargin
  )

}
