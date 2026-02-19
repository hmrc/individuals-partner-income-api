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

import api.controllers.validators.RulesValidator
import api.controllers.validators.resolvers.{ResolveDateRange, ResolvePartnershipName, ResolvePartnershipUtr}
import api.models.domain.TaxYear
import api.models.errors.*
import cats.data.Validated
import v1.createAmendPartnerIncome.model.request.CreateAmendPartnerIncomeRequestData
import java.time.LocalDate
object CreateAmendPartnerIncomeRulesValidator extends RulesValidator[CreateAmendPartnerIncomeRequestData] {

  def validateBusinessRules(parsed: CreateAmendPartnerIncomeRequestData): Validated[Seq[MtdError], CreateAmendPartnerIncomeRequestData] = {

    import parsed.body.*
    import parsed.taxYear

    combine(
      ResolvePartnershipUtr(partnershipUtr, PartnershipUtrFormatError.withPath("/partnershipUtr")),
      ResolvePartnershipName(partnershipName),
      validateStartEndDate(startDate, endDate, taxYear)
    ).onSuccess(parsed)
  }

  def validateStartEndDate(startDate: Option[String], endDate: Option[String], taxYear: TaxYear): Validated[Seq[MtdError], Unit] = {
    val startDatePath = "/startDate"
    val endDatePath   = "/endDate"

    val resolveDateRange: Validated[Seq[MtdError], (Option[LocalDate], Option[LocalDate])] =
      ResolveDateRange(
        StartDateFormatError.withPath(startDatePath),
        EndDateFormatError.withPath(endDatePath)
      )(startDate, endDate)

    def isWithinTaxYear(date: LocalDate): Boolean = TaxYear.containing(date) == taxYear
    resolveDateRange.andThen {
      (start, end) =>

        val validateStart: Validated[Seq[MtdError], Unit] = Validated.cond(start.forall(isWithinTaxYear), (), Seq(RuleStartDateError.withPath("/startDate")))
        val validateEnd: Validated[Seq[MtdError], Unit] = Validated.cond(end.forall(isWithinTaxYear), (), Seq(RuleEndDateError.withPath("/endDate")))
        combine(validateStart, validateEnd)
    }


  }

}
