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

package uk.gov.hmrc.selfassessmentapi.controllers.sandbox

import play.api.libs.json.Json._
import play.api.libs.json._
import reactivemongo.bson.BSONObjectID
import uk.gov.hmrc.selfassessmentapi.domain._

import scala.concurrent.Future
import scala.util.{Failure, Success, Try}

trait SourceHandler[T] {

  implicit val reads: Reads[T]
  implicit val writes: Writes[T]
  def example(id: SourceId): T
  val listName: String

  private def generateId: String = BSONObjectID.generate.stringify

  def create(jsValue: JsValue): Future[Either[ErrorResult, SourceId]] =
    Future.successful (
      Try(jsValue.validate[T]) match {
        case Success(JsSuccess(payload, _)) => Right(generateId)
        case Success(JsError(errors)) =>
          Left(ErrorResult(validationErrors = Some(errors)))
        case Failure(e) =>
          Left(ErrorResult(message = Some(s"could not parse body due to ${e.getMessage}")))
      }
    )

  def findById(sourceId: SourceId): Future[Option[JsValue]] = {
    Future.successful(Some(toJson(example(sourceId))))
  }

  def find: Future[Seq[SourceId]] =
    Future.successful(
      Seq(
        generateId,
        generateId,
        generateId,
        generateId,
        generateId
      )
    )

  def delete(sourceId: SourceId): Future[Boolean] =
    Future.successful(true)

  def update(sourceId: SourceId, jsValue: JsValue): Future[Either[ErrorResult, SourceId]] =
    Future.successful (
      Try(jsValue.validate[T]) match {
        case Success(JsSuccess(payload, _)) => Right(sourceId)
        case Success(JsError(errors)) =>
          Left(ErrorResult(validationErrors = Some(errors)))
        case Failure(e) =>
          Left(ErrorResult(message = Some(s"could not parse body due to ${e.getMessage}")))
      }
    )

  def summaryHandler(summaryType: SummaryType): Option[SummaryHandler[_]]
}

object SelfEmploymentSourceHandler extends SourceHandler[selfemployment.SelfEmployment] {
  override implicit val reads: Reads[selfemployment.SelfEmployment] = selfemployment.SelfEmployment.selfEmploymentReads
  override implicit val writes: Writes[selfemployment.SelfEmployment] = selfemployment.SelfEmployment.selfEmploymentWrites
  override def example(id: SourceId) = selfemployment.SelfEmployment.example.copy(id = Some(id))
  override val listName = SourceTypes.SelfEmployments.name

  override def summaryHandler(summaryType: SummaryType): Option[SummaryHandler[_]] = {
    summaryType match {
      case selfemployment.SummaryTypes.Incomes => Some(IncomesSummaryHandler)
      case selfemployment.SummaryTypes.Expenses => Some(ExpensesSummaryHandler)
      case selfemployment.SummaryTypes.BalancingCharges => Some(BalancingChargesSummaryHandler)
      case selfemployment.SummaryTypes.GoodsAndServicesOwnUse => Some(GoodsAndServiceOwnUseSummaryHandler)
      case _ => None
    }
  }
}

object FurnishedHolidayLettingsSourceHandler extends SourceHandler[furnishedholidaylettings.FurnishedHolidayLettings] {
  override implicit val reads: Reads[furnishedholidaylettings.FurnishedHolidayLettings] = furnishedholidaylettings.FurnishedHolidayLettings.reads
  override implicit val writes: Writes[furnishedholidaylettings.FurnishedHolidayLettings] = furnishedholidaylettings.FurnishedHolidayLettings.writes
  override def example(id: SourceId) = furnishedholidaylettings.FurnishedHolidayLettings.example.copy(id = Some(id))
  override val listName = SourceTypes.FurnishedHolidayLettings.name

  override def summaryHandler(summaryType: SummaryType): Option[SummaryHandler[_]] = {
    summaryType match {
      case furnishedholidaylettings.SummaryTypes.PrivateUseAdjustments => Some(PrivateUseAdjustmentSummaryHandler)
      case furnishedholidaylettings.SummaryTypes.Incomes => Some(FurnishedHolidayLettingsIncomeSummaryHandler)
      case furnishedholidaylettings.SummaryTypes.Expenses => Some(FurnishedHolidayLettingsExpenseSummaryHandler)
      case furnishedholidaylettings.SummaryTypes.BalancingCharges => Some(FurnishedHolidayLettingsBalancingChargesSummaryHandler)
      case _ => None
    }
  }
}

object UKPropertySourceHandler extends SourceHandler[ukproperty.UKProperty] {
  override implicit val reads: Reads[ukproperty.UKProperty] = ukproperty.UKProperty.reads
  override implicit val writes: Writes[ukproperty.UKProperty] = ukproperty.UKProperty.writes
  override def example(id: SourceId) = ukproperty.UKProperty.example.copy(id = Some(id))
  override val listName = SourceTypes.UKProperty.name

  override def summaryHandler(summaryType: SummaryType): Option[SummaryHandler[_]] = {
    summaryType match {
      case ukproperty.SummaryTypes.Incomes => Some(UKPropertyIncomeSummaryHandler)
      case ukproperty.SummaryTypes.Expenses => Some(UKPropertyExpenseSummaryHandler)
      case ukproperty.SummaryTypes.TaxPaid => Some(UKPropertyTaxPaidSummaryHandler)
      case ukproperty.SummaryTypes.BalancingCharges => Some(UKPropertyBalancingChargesSummaryHandler)
      case ukproperty.SummaryTypes.PrivateUseAdjustments => Some(UKPropertyPrivateUseAdjustmentsSummaryHandler)
      case _ => None
    }
  }
}

object EmploymentsSourceHandler extends SourceHandler[employment.Employment] {
  override implicit val reads: Reads[employment.Employment] = employment.Employment.reads
  override implicit val writes: Writes[employment.Employment] = employment.Employment.writes
  override def example(id: SourceId) = employment.Employment.example.copy(id = Some(id))
  override val listName = SourceTypes.Employments.name
  override def summaryHandler(summaryType: SummaryType): Option[SummaryHandler[_]] = None
}

