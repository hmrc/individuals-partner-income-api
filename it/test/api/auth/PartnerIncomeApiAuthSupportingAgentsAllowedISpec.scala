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

package api.auth

import api.services.DownstreamStub
import play.api.libs.json.{JsValue, Json}
import play.api.libs.ws.{WSRequest, WSResponse}

class PartnerIncomeApiAuthSupportingAgentsAllowedISpec extends AuthSupportingAgentsAllowedISpec {

  val callingApiVersion = "1.0"

  val supportingAgentsAllowedEndpoint = "list-partner-income"

  val mtdUrl = s"/$nino/2026-27/partnership"

  def sendMtdRequest(request: WSRequest): WSResponse = await(request.get())

  val downstreamUri = s"/itsa/income-tax/v1/26-27/income/partnerships/$nino/list"

  override val downstreamHttpMethod: DownstreamStub.HTTPMethod = DownstreamStub.GET

  val maybeDownstreamResponseJson: Option[JsValue] = Some(Json.parse(
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
  ))

}
