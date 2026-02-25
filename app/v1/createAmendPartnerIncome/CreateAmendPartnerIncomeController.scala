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

package v1.createAmendPartnerIncome

import api.config.AppConfig
import api.controllers.*
import api.routing.Version
import api.services.{AuditService, EnrolmentsAuthService, MtdIdLookupService}
import api.utils.IdGenerator
import play.api.libs.json.JsValue
import play.api.mvc.{Action, ControllerComponents}

import javax.inject.{Inject, Singleton}
import scala.concurrent.ExecutionContext

@Singleton
class CreateAmendPartnerIncomeController @Inject() (val authService: EnrolmentsAuthService,
                                                    val lookupService: MtdIdLookupService,
                                                    cc: ControllerComponents,
                                                    service: CreateAmendPartnerIncomeService,
                                                    auditService: AuditService,
                                                    validatorFactory: CreateAmendPartnerIncomeValidatorFactory,
                                                    idGenerator: IdGenerator)(implicit appConfig: AppConfig, ec: ExecutionContext)
    extends AuthorisedController(cc) {

  val endpointName: String = "create-amend-partner-income"

  implicit val endpointLogContext: EndpointLogContext =
    EndpointLogContext(
      controllerName = "CreateAmendPartnerIncomeController",
      endpointName = "createAmendPartnerIncome"
    )

  def createAmendPartnerIncome(nino: String, taxYear: String): Action[JsValue] = {
    authorisedAction(nino).async(parse.json) { implicit request =>
      implicit val ctx: RequestContext = RequestContext.from(idGenerator, endpointLogContext)

      val validator = validatorFactory.validator(nino, taxYear, request.body)

      val requestHandler =
        RequestHandler
          .withValidator(validator)
          .withService(service.createAmendPartnerIncome)
          .withAuditing(
            AuditHandler(
              auditService,
              "CreateAmendPartnerIncome",
              "create-amend-partner-income",
              Version(request),
              Map("nino" -> nino, "taxYear" -> taxYear),
              Some(request.body)
            )
          )
          .withNoContentResult()

      requestHandler.handleRequest()
    }
  }

}
