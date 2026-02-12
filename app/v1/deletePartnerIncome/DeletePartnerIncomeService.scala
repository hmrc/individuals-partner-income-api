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

package v1.deletePartnerIncome

import api.controllers.RequestContext
import api.models.errors.*
import api.services.{BaseService, ServiceOutcome}
import cats.implicits.*
import v1.deletePartnerIncome.model.request.DeletePartnerIncomeRequestData

import javax.inject.{Inject, Singleton}
import scala.concurrent.{ExecutionContext, Future}

@Singleton
class DeletePartnerIncomeService @Inject() (connector: DeletePartnerIncomeConnector) extends BaseService {

  def deletePartnerIncome(
      request: DeletePartnerIncomeRequestData)(implicit ctx: RequestContext, ec: ExecutionContext): Future[ServiceOutcome[Unit]] = {

    connector.deletePartnerIncome(request).map(_.leftMap(mapDownstreamErrors(downstreamErrorMap)))

  }

  private val downstreamErrorMap: Map[String, MtdError] = Map(
    "INVALID_TAXABLE_ENTITY_ID" -> NinoFormatError,
    "INVALID_TAX_YEAR"          -> TaxYearFormatError,
    "INVALID_CORRELATION_ID"    -> InternalError,
    "INVALID_PARTNERSHIP_UTR"   -> PartnershipUtrFormatError,
    "NO_DATA_FOUND"             -> NotFoundError,
    "TAX_YEAR_NOT_SUPPORTED"    -> InternalError,
    "OUTSIDE_AMENDMENT_WINDOW"  -> RuleOutsideAmendmentWindowError,
    "SERVER_ERROR"              -> InternalError,
    "SERVICE_UNAVAILABLE"       -> InternalError
  )

}
