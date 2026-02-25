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

import api.controllers.{ControllerBaseSpec, ControllerTestRunner}
import api.models.audit.{AuditEvent, AuditResponse, GenericAuditDetail}
import api.models.errors.{ErrorWrapper, NinoFormatError, TaxYearFormatError}
import api.models.outcomes.ResponseWrapper
import api.services.{MockAuditService, MockEnrolmentsAuthService, MockMtdIdLookupService}
import api.utils.MockIdGenerator
import play.api.Configuration
import play.api.libs.json.JsValue
import play.api.mvc.Result
import play.api.test.FakeRequest
import v1.createAmendPartnerIncome.CreateAmendPartnerIncomeFixtures.{fullMtdJson, request, taxYear}

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future

class CreateAmendPartnerIncomeControllerSpec
    extends ControllerBaseSpec
    with ControllerTestRunner
    with MockEnrolmentsAuthService
    with MockMtdIdLookupService
    with MockCreateAmendPartnerIncomeService
    with MockCreateAmendPartnerIncomeValidatorFactory
    with MockAuditService
    with MockIdGenerator {

  def fakePutRequest[T](body: T): FakeRequest[T] = fakeRequest.withBody(body)

  "createAmendPartnerIncome" when {
    "handling a valid request" should {
      "return a success response with status NO_CONTENT" in new Test {
        willUseValidator(returningSuccess(request))
        MockedCreateAmendPartnerIncomeService
          .createAmendPartnerIncome(request)
          .returns(Future.successful(Right(ResponseWrapper(correlationId, ()))))

        runOkTestWithAudit(NO_CONTENT, maybeAuditRequestBody = Some(fullMtdJson))
      }
    }

    "the parser validation fails" should {
      "return the error per spec" in new Test {
        willUseValidator(returning(NinoFormatError))
        runErrorTestWithAudit(NinoFormatError, Some(fullMtdJson))
      }
    }

    "the service returns an error" should {
      "return the error per spec" in new Test {
        willUseValidator(returningSuccess(request))
        MockedCreateAmendPartnerIncomeService
          .createAmendPartnerIncome(request)
          .returns(Future.successful(Left(ErrorWrapper(correlationId, TaxYearFormatError))))
        runErrorTestWithAudit(TaxYearFormatError, Some(fullMtdJson))
      }
    }
  }

  trait Test extends ControllerTest with AuditEventChecking[GenericAuditDetail] {

    protected val controller: CreateAmendPartnerIncomeController = new CreateAmendPartnerIncomeController(
      mockEnrolmentsAuthService,
      mockMtdIdLookupService,
      cc,
      mockService,
      mockAuditService,
      mockValidatorFactory,
      mockIdGenerator
    )

    MockedAppConfig.featureSwitchConfig.anyNumberOfTimes() returns Configuration(
      "supporting-agents-access-control.enabled" -> true
    )

    MockedAppConfig.endpointAllowsSupportingAgents(controller.endpointName).anyNumberOfTimes() returns false

    protected def callController(): Future[Result] =
      controller.createAmendPartnerIncome(validNino, taxYear.asMtd)(fakePutRequest(fullMtdJson))

    protected def event(auditResponse: AuditResponse, maybeRequestBody: Option[JsValue]): AuditEvent[GenericAuditDetail] =
      AuditEvent(
        auditType = "CreateAmendPartnerIncome",
        transactionName = "create-amend-partner-income",
        detail = GenericAuditDetail(
          versionNumber = apiVersion.name,
          userType = "Individual",
          agentReferenceNumber = None,
          params = Map("nino" -> validNino, "taxYear" -> taxYear.asMtd),
          requestBody = maybeRequestBody,
          `X-CorrelationId` = correlationId,
          auditResponse = auditResponse
        )
      )

  }

}
