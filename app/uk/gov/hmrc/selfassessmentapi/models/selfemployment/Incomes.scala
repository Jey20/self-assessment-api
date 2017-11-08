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

package uk.gov.hmrc.selfassessmentapi.models.selfemployment

import play.api.libs.functional.syntax._
import play.api.libs.json.{Json, Reads, Writes, __}
import uk.gov.hmrc.selfassessmentapi.models.{Amount, nonNegativeAmountValidator}

case class Incomes(turnover: Option[Amount] = None, other: Option[Amount] = None) {

  def hasIncomes: Boolean =
    turnover.isDefined || other.isDefined
}

object Incomes {
  implicit val writes: Writes[Incomes] = Json.writes[Incomes]
  implicit val reads: Reads[Incomes] = (
                      (__ \ "turnover").readNullable[Amount](nonNegativeAmountValidator) and
                      (__ \ "other").readNullable[Amount](nonNegativeAmountValidator)
                    ) (Incomes.apply _)
}
