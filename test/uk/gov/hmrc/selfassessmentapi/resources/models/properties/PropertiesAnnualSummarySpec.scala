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

package uk.gov.hmrc.selfassessmentapi.resources.models.properties

import uk.gov.hmrc.selfassessmentapi.controllers.api.JsonSpec

class PropertiesAnnualSummarySpec extends JsonSpec {
  "OtherPropertiesAnnualSummary" should {
    "round trip" in {
      roundTripJson(
        OtherPropertiesAnnualSummary(
          allowances = Some(Allowances(Some(50), Some(20.20), Some(15.15), Some(12.34))),
          adjustments = Some(Adjustments(Some(20.23), Some(50.55), Some(12.34), Some(57.55)))))
    }
  }

  "FHLPropertiesAnnualSummary" should {
    "round trip" in {
      roundTripJson(
        FHLPropertiesAnnualSummary(
          allowances = Some(Allowances(Some(50), Some(20.20), Some(15.15), Some(12.34))),
          adjustments = Some(Adjustments(Some(20.23), Some(50.55), Some(12.34), Some(57.55)))))
    }
  }
}