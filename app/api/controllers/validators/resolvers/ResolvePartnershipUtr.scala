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

package api.controllers.validators.resolvers

import api.models.domain.PartnershipUtr
import api.models.errors.{MtdError, PartnershipUtrFormatError}
import cats.data.Validated

case class ResolvePartnershipUtr(formatError: MtdError) extends ResolverSupport {

  private val partnershipUtrRegex = "^[0-9]{10}$".r

  val resolver: Resolver[String, PartnershipUtr] =
    ResolveStringPattern(partnershipUtrRegex, formatError).resolver.map(PartnershipUtr.apply)

}

object ResolvePartnershipUtr extends ResolverSupport {

  def apply(value: String, formatError: MtdError = PartnershipUtrFormatError): Validated[Seq[MtdError], PartnershipUtr] =
    ResolvePartnershipUtr(formatError).resolver(value)

}
