/*
 * Copyright 2017 HM Revenue & Customs
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

package uk.gov.hmrc.selfassessmentapi.controllers.api.ukproperty

import play.api.libs.json._
import uk.gov.hmrc.selfassessmentapi.controllers.api._

case class Adjustments(lossBroughtForward: Option[BigDecimal] = None)

object Adjustments {
  implicit val writes = Json.writes[Adjustments]

  implicit val reads: Reads[Adjustments] = (__ \ "lossBroughtForward").readNullable[BigDecimal](positiveAmountValidator("lossBroughtForward")).map {
    Adjustments(_)
  }
}