/*
 * Copyright 2016 HM Revenue & Customs
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

package uk.gov.hmrc.selfassessmentapi.domain

import uk.gov.hmrc.selfassessmentapi.domain.SelfEmploymentIncomeType._

class SelfEmploymentIncomeSpec extends JsonSpec {

  "format" should {

    "round trip valid SelfEmploymentIncome json" in {
      roundTripJson(SelfEmploymentIncome(`type` = TURNOVER, amount = BigDecimal(1000.99)))
    }
  }

  "validate" should {
    "reject amounts with more than 2 decimal values" in {
      Seq(BigDecimal(1000.123), BigDecimal(1000.1234), BigDecimal(1000.12345), BigDecimal(1000.123456789)).foreach { testAmount =>
        val seIncome = SelfEmploymentIncome(`type` = TURNOVER, amount = testAmount)
        assertValidationError[SelfEmploymentIncome](
          seIncome,
          Map(ErrorCode("INVALID_MONETARY_AMOUNT") -> "amount should be non-negative number up to 2 decimal values"),
          "Expected self-employment-income with up to 2 decimal places")
      }
    }

    "reject negative amount" in {
      val seIncome = SelfEmploymentIncome(`type` = TURNOVER, amount = BigDecimal(-1000.12))
      assertValidationError[SelfEmploymentIncome](
        seIncome,
        Map(ErrorCode("INVALID_MONETARY_AMOUNT") -> "amount should be non-negative number up to 2 decimal values"),
        "Expected positive self-employment-income")
    }
  }

}
