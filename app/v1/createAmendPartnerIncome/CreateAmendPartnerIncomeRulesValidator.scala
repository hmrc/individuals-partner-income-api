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
import api.controllers.validators.resolvers.{ResolveDateRange, ResolveParsedNumber, ResolvePartnershipUtr, ResolveStringPattern}
import api.models.domain.TaxYear
import api.models.errors.*
import cats.data.Validated
import cats.data.Validated.Invalid
import cats.syntax.all.*
import v1.createAmendPartnerIncome.model.request.{CreateAmendPartnerIncomeRequestBody, CreateAmendPartnerIncomeRequestData, PartnerShipTrade}
import java.time.LocalDate

object CreateAmendPartnerIncomeRulesValidator extends RulesValidator[CreateAmendPartnerIncomeRequestData] {

  def validateBusinessRules(parsed: CreateAmendPartnerIncomeRequestData): Validated[Seq[MtdError], CreateAmendPartnerIncomeRequestData] = {

    import parsed.body.*
    import parsed.taxYear

    combine(
      ResolvePartnershipUtr(partnershipUtr, PartnershipUtrFormatError.withPath("/partnershipUtr")),
      validatePartnershipName(partnershipName),
      validateStartEndDate(startDate, endDate, taxYear),
      validatePartnershipTrades(partnershipTrades),
      validateNumericValues(parsed.body)
    ).onSuccess(parsed)
  }

  private val resolveNonNegativeParsedNumber   = ResolveParsedNumber()
  private val resolveMaybeNegativeParsedNumber = ResolveParsedNumber(min = -99999999999.99)

  private val partnershipNameRegex = "^.{1,105}$".r

  private def validatePartnershipName(name: String): Validated[Seq[MtdError], String] =
    ResolveStringPattern(partnershipNameRegex, PartnershipNameFormatError.withPath("/partnershipName"))(name)

  private def validateStartEndDate(startDate: Option[String], endDate: Option[String], taxYear: TaxYear): Validated[Seq[MtdError], Unit] = {
    val startDatePath = "/startDate"
    val endDatePath   = "/endDate"

    val resolveDateRange: Validated[Seq[MtdError], (Option[LocalDate], Option[LocalDate])] =
      ResolveDateRange(
        StartDateFormatError.withPath(startDatePath),
        EndDateFormatError.withPath(endDatePath)
      )(startDate, endDate)

    def isWithinTaxYear(date: LocalDate): Boolean = TaxYear.containing(date) == taxYear
    resolveDateRange.andThen { (start, end) =>
      val validateStart = Validated.cond(start.forall(isWithinTaxYear), (), Seq(RuleStartDateError.withPath(startDatePath)))
      val validateEnd   = Validated.cond(end.forall(isWithinTaxYear), (), Seq(RuleEndDateError.withPath(endDatePath)))
      combine(validateStart, validateEnd)
    }

  }

  private def validateTradeDescription(partnershipTrades: Option[Seq[PartnerShipTrade]]): Validated[Seq[MtdError], Unit] = {
    val tradeDescriptionRegex = "^.{1,30}$".r
    val indexed = partnershipTrades
      .getOrElse(Seq.empty)
      .map(_.tradeDescription)
      .zipWithIndex

    indexed
      .traverse { case (tradeDescription, index) =>
        ResolveStringPattern(tradeDescriptionRegex, TradeDescriptionFormatError.withPath(s"/partnershipTrades/$index/tradeDescription"))(
          tradeDescription)
      }
      .andThen { tradeDescriptions =>
        val duplicates = indexed
          .groupBy(_._1)
          .collect {
            case (value, pairs) if pairs.size > 1 => value
          }
          .toSet
        val validated = indexed.map { case (value, idx) =>
          if (duplicates(value))
            Invalid(List(RuleDuplicateTradeDescriptionError.withPath(s"/partnershipTrades/$idx/tradeDescription")))
          else
            valid
        }

        validated.sequence.andThen(_ => valid)

      }

  }

  private def validatePartnershipTradeDetails(partnershipTrades: Option[Seq[PartnerShipTrade]]): Validated[Seq[MtdError], Unit] = {
    partnershipTrades
      .getOrElse(Seq.empty)
      .zipWithIndex
      .map { case (partnerShipTrade, idx) =>
        if (partnerShipTrade.tradingOrProfessionalLosses.isEmpty && partnerShipTrade.tradingOrProfessionalProfits.isEmpty) {
          Invalid(List(RuleMissingPartnershipTradesDetailsError.withPath(s"/partnershipTrades/$idx")))
        } else {
          valid
        }
      }
      .sequence
      .andThen(_ => valid)
  }

  private def validatePartnershipTradeNumericValues(partnershipTrades: Option[Seq[PartnerShipTrade]]): Validated[Seq[MtdError], Seq[Unit]] = {
    partnershipTrades
      .getOrElse(Seq.empty)
      .zipWithIndex
      .traverse { case (partnerShipTrade, idx) =>
        import partnerShipTrade.*

        val nonNegative = List(
          (tradingOrProfessionalProfits.map(_.adjustedProfit), s"/partnershipTrades/$idx/tradingOrProfessionalProfits/adjustedProfit"),
          (tradingOrProfessionalProfits.map(_.shareOfTaxableProfit), s"/partnershipTrades/$idx/tradingOrProfessionalProfits/shareOfTaxableProfit")
        )

        val optNonNegative = List(
          (
            tradingOrProfessionalProfits.flatMap(_.accountingAdjustment),
            s"/partnershipTrades/$idx/tradingOrProfessionalProfits/accountingAdjustment"),
          (
            tradingOrProfessionalProfits.flatMap(_.foreignTaxClaimedDeduction),
            s"/partnershipTrades/$idx/tradingOrProfessionalProfits/foreignTaxClaimedDeduction"),
          (
            tradingOrProfessionalProfits.flatMap(_.transitionProfitArisingThisYear),
            s"/partnershipTrades/$idx/tradingOrProfessionalProfits/transitionProfitArisingThisYear"),
          (
            tradingOrProfessionalProfits.flatMap(_.lossesBroughtForwardTransitionProfit),
            s"/partnershipTrades/$idx/tradingOrProfessionalProfits/lossesBroughtForwardTransitionProfit"),
          (
            tradingOrProfessionalProfits.flatMap(_.lossesBroughtForwardAdjustedProfit),
            s"/partnershipTrades/$idx/tradingOrProfessionalProfits/lossesBroughtForwardAdjustedProfit"),
          (
            tradingOrProfessionalProfits.flatMap(_.profitAfterBroughtForwardLosses),
            s"/partnershipTrades/$idx/tradingOrProfessionalProfits/profitAfterBroughtForwardLosses"),
          (tradingOrProfessionalProfits.flatMap(_.otherBusinessIncome), s"/partnershipTrades/$idx/tradingOrProfessionalProfits/otherBusinessIncome"),
          (tradingOrProfessionalLosses.flatMap(_.adjustedLoss), s"/partnershipTrades/$idx/tradingOrProfessionalLosses/adjustedLoss"),
          (
            tradingOrProfessionalLosses.flatMap(_.currentYearLossAppliedToGeneralIncome),
            s"/partnershipTrades/$idx/tradingOrProfessionalLosses/currentYearLossAppliedToGeneralIncome"),
          (tradingOrProfessionalLosses.flatMap(_.lossesCarriedBack), s"/partnershipTrades/$idx/tradingOrProfessionalLosses/lossesCarriedBack"),
          (tradingOrProfessionalLosses.flatMap(_.carryForwardLosses), s"/partnershipTrades/$idx/tradingOrProfessionalLosses/carryForwardLosses")
        )

        val negative =
          (tradingOrProfessionalProfits.map(_.shareOfProfitOrLoss), s"/partnershipTrades/$idx/tradingOrProfessionalProfits/shareOfProfitOrLoss")

        val optNegative = List(
          (tradingOrProfessionalProfits.flatMap(_.basisAdjustment), s"/partnershipTrades/0/tradingOrProfessionalProfits/basisAdjustment"),
          (tradingOrProfessionalProfits.flatMap(_.averagingAdjustment), s"/partnershipTrades/0/tradingOrProfessionalProfits/averagingAdjustment")
        )

        val validateNonNegative: List[Validated[Seq[MtdError], Option[BigDecimal]]] = nonNegative.map((n, p) => resolveNonNegativeParsedNumber(n, p))
        val validateOptNonNegative: List[Validated[Seq[MtdError], Option[BigDecimal]]] =
          optNonNegative.map((n, p) => resolveNonNegativeParsedNumber(n, p))
        val validateOptNegative: List[Validated[Seq[MtdError], Option[BigDecimal]]] =
          optNegative.map((n, p) => resolveMaybeNegativeParsedNumber(n, p))
        val validateNegative: Validated[Seq[MtdError], Option[BigDecimal]] = resolveMaybeNegativeParsedNumber(negative._1, negative._2)

        (validateNonNegative ++ validateOptNonNegative ++ validateOptNegative :+ validateNegative).sequence.andThen(_ => valid)
      }

  }

  def validatePartnershipTrades(partnershipTrades: Option[Seq[PartnerShipTrade]]): Validated[Seq[MtdError], Unit] = {
    combine(
      validatePartnershipTradeDetails(partnershipTrades),
      validateTradeDescription(partnershipTrades),
      validatePartnershipTradeNumericValues(partnershipTrades)
    )
  }

  private def validateNumericValues(body: CreateAmendPartnerIncomeRequestBody): Validated[Seq[MtdError], Unit] = {

    import body.*

    val nonNegative = List(
      (
        totalUntaxedIncomeExcludingSavings.map(_.shareOfTotalUntaxedIncomeExcludingSavings),
        "/totalUntaxedIncomeExcludingSavings/shareOfTotalUntaxedIncomeExcludingSavings"),
      (totalTaxedAndUntaxedIncome.map(_.shareOfTotalTaxedAndUntaxedIncome), "/totalTaxedAndUntaxedIncome/shareOfTotalTaxedAndUntaxedIncome")
    )

    val optNonNegative = List(
      (nationalInsuranceContributions.flatMap(_.adjustmentToProfitsForClass4), "/nationalInsuranceContributions/adjustmentToProfitsForClass4"),
      (untaxedSavingsIncome.flatMap(_.shareOfUkUntaxedSavingsIncome), "/untaxedSavingsIncome/shareOfUkUntaxedSavingsIncome"),
      (untaxedSavingsIncome.flatMap(_.adjustedUkSavingsIncome), "/untaxedSavingsIncome/adjustedUkSavingsIncome"),
      (untaxedSavingsIncome.flatMap(_.shareOfForeignUntaxedSavingsIncome), "/untaxedSavingsIncome/shareOfForeignUntaxedSavingsIncome"),
      (untaxedSavingsIncome.flatMap(_.totalForeignTaxTakenOff), "/untaxedSavingsIncome/totalForeignTaxTakenOff"),
      (untaxedSavingsIncome.flatMap(_.adjustedForeignSavingsIncome), "/untaxedSavingsIncome/adjustedForeignSavingsIncome"),
      (untaxedSavingsIncome.flatMap(_.totalUntaxedSavingsIncome), "/untaxedSavingsIncome/totalUntaxedSavingsIncome"),
      (ukPropertyIncome.flatMap(_.lossesBroughtForward), "/ukPropertyIncome/lossesBroughtForward"),
      (ukPropertyIncome.flatMap(_.currentYearLossAppliedToGeneralIncome), "/ukPropertyIncome/currentYearLossAppliedToGeneralIncome"),
      (ukPropertyIncome.flatMap(_.carryForwardLosses), "/ukPropertyIncome/carryForwardLosses"),
      (ukPropertyIncome.flatMap(_.shareOfTaxableProfit), "/ukPropertyIncome/shareOfTaxableProfit"),
      (ukPropertyIncome.flatMap(_.residentialPropertyFinanceCosts), "/ukPropertyIncome/residentialPropertyFinanceCosts"),
      (ukPropertyIncome.flatMap(_.residentialPropertyFinanceCostsBroughtForward), "/ukPropertyIncome/residentialPropertyFinanceCostsBroughtForward"),
      (otherUntaxedUkIncome.flatMap(_.shareOfOtherUntaxedUkIncome), "/otherUntaxedUkIncome/shareOfOtherUntaxedUkIncome"),
      (otherUntaxedUkIncome.flatMap(_.lossesBroughtForward), "/otherUntaxedUkIncome/lossesBroughtForward"),
      (otherUntaxedUkIncome.flatMap(_.shareOfTaxableProfit), "/otherUntaxedUkIncome/shareOfTaxableProfit"),
      (otherUntaxedUkIncome.flatMap(_.shareOfLoss), "/otherUntaxedUkIncome/shareOfLoss"),
      (otherUntaxedUkIncome.flatMap(_.carryForwardLosses), "/otherUntaxedUkIncome/carryForwardLosses"),
      (offshoreFundsIncome.flatMap(_.shareOfOffshoreFundsIncome), "/offshoreFundsIncome/shareOfOffshoreFundsIncome"),
      (offshoreFundsIncome.flatMap(_.totalForeignTaxTakenOff), "/offshoreFundsIncome/totalForeignTaxTakenOff"),
      (offshoreFundsIncome.flatMap(_.shareOfTaxableIncome), "/offshoreFundsIncome/shareOfTaxableIncome"),
      (otherUntaxedForeignIncome.flatMap(_.shareOfOtherUntaxedForeignIncome), "/otherUntaxedForeignIncome/shareOfOtherUntaxedForeignIncome"),
      (otherUntaxedForeignIncome.flatMap(_.lossesBroughtForward), "/otherUntaxedForeignIncome/lossesBroughtForward"),
      (otherUntaxedForeignIncome.flatMap(_.totalForeignTaxTakenOff), "/otherUntaxedForeignIncome/totalForeignTaxTakenOff"),
      (otherUntaxedForeignIncome.flatMap(_.shareOfTaxableProfit), "/otherUntaxedForeignIncome/shareOfTaxableProfit"),
      (otherUntaxedForeignIncome.flatMap(_.shareOfLoss), "/otherUntaxedForeignIncome/shareOfLoss"),
      (otherUntaxedForeignIncome.flatMap(_.carryForwardLosses), "/otherUntaxedForeignIncome/carryForwardLosses"),
      (otherUntaxedForeignIncome.flatMap(_.residentialPropertyFinanceCosts), "/otherUntaxedForeignIncome/residentialPropertyFinanceCosts"),
      (
        otherUntaxedForeignIncome.flatMap(_.residentialPropertyFinanceCostsBroughtForward),
        "/otherUntaxedForeignIncome/residentialPropertyFinanceCostsBroughtForward"),
      (taxedIncomeAndDividendIncome.flatMap(_.shareOfDividendIncome), "/taxedIncomeAndDividendIncome/shareOfDividendIncome"),
      (taxedIncomeAndDividendIncome.flatMap(_.dividendIncomeForeignTaxTakenOff), "/taxedIncomeAndDividendIncome/dividendIncomeForeignTaxTakenOff"),
      (taxedIncomeAndDividendIncome.flatMap(_.totalDividendIncome), "/taxedIncomeAndDividendIncome/totalDividendIncome"),
      (taxedIncomeAndDividendIncome.flatMap(_.shareOfTaxedIncome), "/taxedIncomeAndDividendIncome/shareOfTaxedIncome"),
      (taxedIncomeAndDividendIncome.flatMap(_.taxedIncomeForeignTaxTakenOff), "/taxedIncomeAndDividendIncome/taxedIncomeForeignTaxTakenOff"),
      (taxedIncomeAndDividendIncome.flatMap(_.totalTaxedIncome), "/taxedIncomeAndDividendIncome/totalTaxedIncome"),
      (taxedIncomeAndDividendIncome.flatMap(_.shareOfOtherTaxedIncome), "/taxedIncomeAndDividendIncome/shareOfOtherTaxedIncome"),
      (
        taxedIncomeAndDividendIncome.flatMap(_.otherTaxedIncomeForeignTaxTakenOff),
        "/taxedIncomeAndDividendIncome/otherTaxedIncomeForeignTaxTakenOff"),
      (taxPaidAndDeductions.flatMap(_.shareOfIncomeTaxTakenOffPartnershipIncome), "/taxPaidAndDeductions/shareOfIncomeTaxTakenOffPartnershipIncome"),
      (taxPaidAndDeductions.flatMap(_.shareOfCisDeductionsByContractors), "/taxPaidAndDeductions/shareOfCisDeductionsByContractors"),
      (taxPaidAndDeductions.flatMap(_.shareOfTaxTakenOffTradingIncome), "/taxPaidAndDeductions/shareOfTaxTakenOffTradingIncome"),
      (taxPaidAndDeductions.flatMap(_.shareOfTotalTaxTakenOff), "/taxPaidAndDeductions/shareOfTotalTaxTakenOff")
    )

    val optNegative = List(
      (untaxedSavingsIncome.flatMap(_.untaxedSavingsIncomeAdjustment), "/untaxedSavingsIncome/untaxedSavingsIncomeAdjustment"),
      (untaxedSavingsIncome.flatMap(_.foreignUntaxedSavingsIncomeAdjustment), "/untaxedSavingsIncome/foreignUntaxedSavingsIncomeAdjustment"),
      (ukPropertyIncome.flatMap(_.shareOfProfitOrLoss), "/ukPropertyIncome/shareOfProfitOrLoss"),
      (ukPropertyIncome.flatMap(_.profitOrLossAdjustment), "/ukPropertyIncome/profitOrLossAdjustment"),
      (otherUntaxedUkIncome.flatMap(_.otherUntaxedUkIncomeAdjustment), "/otherUntaxedUkIncome/otherUntaxedUkIncomeAdjustment"),
      (otherUntaxedUkIncome.flatMap(_.otherUntaxedUkIncomeLossAdjustment), "/otherUntaxedUkIncome/otherUntaxedUkIncomeLossAdjustment"),
      (offshoreFundsIncome.flatMap(_.offshoreFundsIncomeAdjustment), "/offshoreFundsIncome/offshoreFundsIncomeAdjustment"),
      (otherUntaxedForeignIncome.flatMap(_.otherUntaxedForeignIncomeAdjustment), "/otherUntaxedForeignIncome/otherUntaxedForeignIncomeAdjustment"),
      (
        otherUntaxedForeignIncome.flatMap(_.otherUntaxedForeignIncomeLossesAdjustment),
        "/otherUntaxedForeignIncome/otherUntaxedForeignIncomeLossesAdjustment")
    )

    val validateNonNegative = nonNegative.map((n, p) => resolveNonNegativeParsedNumber(n, p))

    val validateOptNonNegative = optNonNegative.map((n, p) => resolveNonNegativeParsedNumber(n, p))

    val validateOptNegative = optNegative.map((n, p) => resolveMaybeNegativeParsedNumber(n, p))

    (validateNonNegative ++ validateOptNonNegative ++ validateOptNegative).sequence.andThen(_ => valid)
  }

}
