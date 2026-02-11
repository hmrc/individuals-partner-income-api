package v1.list

import api.utils.UnitSpec
import v1.list.ListPartnerIncomeFixtures.*

class ListPartnerIncomeValidatorFactorySpec extends UnitSpec {

  private val validatorFactory = new ListPartnerIncomeValidatorFactory

  "validator()" should {
    "return the validator for list partner income" in {
      val result = validatorFactory.validator(nino.nino, taxYear.asMtd)
      result shouldBe a[ListPartnerIncomeValidator]
    }
  }
}
