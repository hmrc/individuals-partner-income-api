package v1.list

import api.controllers.validators.{MockValidatorFactory, Validator}
import org.scalamock.handlers.CallHandler
import v1.list.model.request.ListPartnerIncomeRequestData

trait MockListPartnerIncomeValidatorFactory extends MockValidatorFactory[ListPartnerIncomeRequestData] {
  val mockValidatorFactory: ListPartnerIncomeValidatorFactory = mock[ListPartnerIncomeValidatorFactory]
  
  def validator(): CallHandler[Validator[ListPartnerIncomeRequestData]] =
    (mockValidatorFactory.validator(_: String, _: String)).expects(*, *)

}
