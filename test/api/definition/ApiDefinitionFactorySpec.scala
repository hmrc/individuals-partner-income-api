/*
 * Copyright 2023 HM Revenue & Customs
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

package api.definition

import cats.implicits.catsSyntaxValidatedId
import api.config.Deprecation.NotDeprecated
import api.config.MockAppConfig
import api.definition.APIStatus.{ALPHA, BETA}
import api.mocks.MockHttpClient
import api.routing.*
import api.utils.UnitSpec

import scala.language.reflectiveCalls

class ApiDefinitionFactorySpec extends UnitSpec {

  "buildAPIStatus" when {
    "the 'apiStatus' parameter is present and valid" should {
      s"return the expected status" in new Test {
        MockedAppConfig.apiStatus(Version1) returns "BETA"
        MockedAppConfig.deprecationFor(Version1).returns(NotDeprecated.valid).anyNumberOfTimes()

        val result: APIStatus = apiDefinitionFactory.buildAPIStatus(Version1)
        result shouldBe BETA
      }

    }

    "the 'apiStatus' parameter is present but invalid" should {
      s"default to alpha" in new Test {
        MockedAppConfig.apiStatus(Version1) returns "not-a-status"
        MockedAppConfig.deprecationFor(Version1).returns(NotDeprecated.valid).anyNumberOfTimes()
        apiDefinitionFactory.buildAPIStatus(Version1) shouldBe ALPHA
      }
    }

    "the 'deprecatedOn' parameter is missing for a deprecated version" should {
      "throw an exception" in new Test {
        MockedAppConfig.apiStatus(Version1) returns "DEPRECATED"
        MockedAppConfig
          .deprecationFor(Version1)
          .returns("deprecatedOn date is required for a deprecated version".invalid)
          .anyNumberOfTimes()

        val exception: Exception = intercept[Exception] {
          apiDefinitionFactory.buildAPIStatus(Version1)
        }

        val exceptionMessage: String = exception.getMessage
        exceptionMessage shouldBe "deprecatedOn date is required for a deprecated version"
      }
    }
  }

  class Test extends MockHttpClient with MockAppConfig {
    val apiDefinitionFactory = new ApiDefinitionFactory(mockAppConfig)
    MockedAppConfig.apiGatewayContext returns "individuals/self-assessment/adjustable-summary"
  }

}
