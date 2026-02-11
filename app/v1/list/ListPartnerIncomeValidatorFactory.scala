package v1.list

import api.controllers.validators.Validator
import v1.list.model.request.ListPartnerIncomeRequestData

import javax.inject.Singleton

@Singleton
class ListPartnerIncomeValidatorFactory {
  def validator(nino: String, taxYear: String): Validator[ListPartnerIncomeRequestData] = new ListPartnerIncomeValidator(nino, taxYear)
}
