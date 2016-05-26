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

package uk.gov.hmrc.selfassessmentapi.domain.furnishedholidaylettings

import play.api.libs.json.Json
import uk.gov.hmrc.selfassessmentapi.domain.ErrorCode._
import uk.gov.hmrc.selfassessmentapi.domain.JsonSpec
import ExpenseType.PremisesRunningCosts

class ExpenseSpec extends JsonSpec {

  "format" should {
    "round trip Expense json" in {
      ExpenseType.values.foreach { expenseType =>
        roundTripJson(Expense(`type` = expenseType, amount = BigDecimal(1000)))
      }
    }
  }

  "validate" should {

    "reject invalid Expense type" in {
      val json = Json.parse(
        """
          |{ "type": "Blah",
          |"amount" : 10000.45
          |}
        """.stripMargin)

      assertValidationError[Expense](
        json,
        Map(("/type", NO_VALUE_FOUND) -> "Furnished Holiday Lettings Expense type is invalid"),
        "Expected invalid furnished-holiday-lettings-expense")
    }

    "reject amounts with more than 2 decimal values" in {
      Seq(BigDecimal(1000.123), BigDecimal(1000.12456), BigDecimal(1000.123454), BigDecimal(1000.123456789)).foreach { testAmount =>
        val expense = Expense(`type` = PremisesRunningCosts, amount = testAmount)
        assertValidationError[Expense](
          expense,
          Map(("/amount", INVALID_MONETARY_AMOUNT) -> "amount should be non-negative number up to 2 decimal values"),
          "Expected invalid furnished-holiday-lettings-expense")
      }
    }

    "reject negative amount" in {
      val expense = Expense(`type` = PremisesRunningCosts, amount = BigDecimal(-1000.13))
      assertValidationError[Expense](
        expense,
        Map(("/amount", INVALID_MONETARY_AMOUNT) -> "amount should be non-negative number up to 2 decimal values"),
        "Expected negative furnished-holiday-lettings-expense")
    }
  }
}
