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

package uk.gov.hmrc.selfassessmentapi

import play.api.data.validation.ValidationError
import play.api.libs.json._
import uk.gov.hmrc.selfassessmentapi.domain.CountryCodes.{apply => _, _}
import uk.gov.hmrc.selfassessmentapi.domain.ErrorCode._
import uk.gov.hmrc.selfassessmentapi.domain.UkCountryCodes.{apply => _, _}


package object domain {

  type SourceId = String
  type SummaryId = String
  type LiabilityId = String
  type ValidationErrors = Seq[(JsPath, Seq[ValidationError])]
  type GenericCountryCode = Either[CountryCode, UkCountryCode]

  def lengthValidator = Reads.of[String].filter(ValidationError("field length exceeded the max 100 chars", MAX_FIELD_LENGTH_EXCEEDED))(_.length <= 100)

  def positiveAmountValidator(fieldName: String) = Reads.of[BigDecimal].filter(ValidationError(s"$fieldName should be non-negative number up to 2 decimal values",
    INVALID_MONETARY_AMOUNT))(x => x >= 0 && x.scale < 3)

  def amountValidator(fieldName: String) = Reads.of[BigDecimal].filter(ValidationError(s"$fieldName should be a number up to 2 decimal values",
    INVALID_MONETARY_AMOUNT))(x => x.scale < 3)

  def maxAmountValidator(fieldName: String, maxAmount: BigDecimal) = Reads.of[BigDecimal].filter(ValidationError(s"$fieldName should be less or equal than $maxAmount",
    MAX_MONETARY_AMOUNT))(_ <= maxAmount)

  implicit def eitherReads[A, B](implicit A: Reads[A], B: Reads[B]): Reads[Either[A, B]] =
    Reads[Either[A, B]] { json =>
      A.reads(json) match {
        case JsSuccess(value, path) => JsSuccess(Left(value), path)
        case JsError(e1) => B.reads(json) match {
          case JsSuccess(value, path) => JsSuccess(Right(value), path)
          case JsError(e2) => JsError(JsError.merge(e1, e2))
        }
      }
    }

  implicit def eitherWrites[A, B](implicit A: Writes[A], B: Writes[B]): Writes[Either[A, B]] =
    Writes[Either[A, B]] { obj =>
      if (obj.isLeft)
        JsString(obj.left.get.toString)
      else JsString(obj.right.get.toString)
    }
}
