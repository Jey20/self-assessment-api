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

import play.api.libs.json.Json

class BalancingChargeSpec extends JsonSpec {

  "format" should {
    "round trip valid BalancingCharge json" in {
      roundTripJson(BalancingCharge(None, BalancingChargeType.Other, BigDecimal(100.12)))
      roundTripJson(BalancingCharge(None, BalancingChargeType.BPRA, BigDecimal(100.12)))
    }
  }

  "validate" should {
    "reject an amount which is more than 2 decimal places" in {
      val balancingCharge = BalancingCharge(None, BalancingChargeType.Other, BigDecimal(100.123))
      assertValidationError[BalancingCharge](
        balancingCharge,
        Map(ErrorCode("INVALID_MONETARY_AMOUNT") -> "amount should be non-negative number up to 2 decimal values"),
        "should fail with INVALID_MONETARY_AMOUNT error")
    }

    "reject an negative amount" in {
      val balancingCharge = BalancingCharge(None, BalancingChargeType.BPRA, BigDecimal(-100.12))
      assertValidationError[BalancingCharge](
        balancingCharge,
        Map(ErrorCode("INVALID_MONETARY_AMOUNT") -> "amount should be non-negative number up to 2 decimal values"),
        "should fail with INVALID_MONETARY_AMOUNT error")
    }

    "reject invalid Balancing charge category" in {
      val json = Json.parse(
        """
          |{ "type": "BAZ",
          |"amount" : 10000.45
          |}
        """.
          stripMargin)

      assertValidationError[BalancingCharge](
        json,
        Map(ErrorCode("NO_VALUE_FOUND") -> "Self Employment Balancing charge type is invalid"),
        "should fail with NO_VALUE_FOUND error")
    }

  }
}
