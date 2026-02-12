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

import api.controllers.validators.Validator
import api.controllers.validators.resolvers.{ResolveDetailedTaxYear, ResolveNino, ResolvePartnershipUtr}
import api.models.domain.TaxYear
import api.models.errors.MtdError
import cats.data.Validated
import cats.implicits.catsSyntaxTuple3Semigroupal
import v1.deletePartnerIncome.model.request.DeletePartnerIncomeRequestData
import v1.minimumTaxYear

import javax.inject.Singleton

@Singleton
class DeletePartnerIncomeValidator(nino: String, taxYear: String, partnershipUtr: String) extends Validator[DeletePartnerIncomeRequestData] {

  private val resolveTaxYear = ResolveDetailedTaxYear(minimumTaxYear)

  def validate: Validated[Seq[MtdError], DeletePartnerIncomeRequestData] =
    (
      ResolveNino(nino),
      resolveTaxYear(taxYear),
      ResolvePartnershipUtr(partnershipUtr)
    ).mapN(DeletePartnerIncomeRequestData.apply)

}
