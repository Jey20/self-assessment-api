/*
 * Copyright 2018 HM Revenue & Customs
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

package uk.gov.hmrc.selfassessmentapi.connectors

import uk.gov.hmrc.domain.Nino
import uk.gov.hmrc.http.HeaderCarrier
import uk.gov.hmrc.selfassessmentapi.config.AppContext
import uk.gov.hmrc.selfassessmentapi.models.des.selfemployment.RequestDateTime
import uk.gov.hmrc.selfassessmentapi.models.{Period, SourceId}
import uk.gov.hmrc.selfassessmentapi.resources.utils.ObligationQueryParams
import uk.gov.hmrc.selfassessmentapi.resources.wrappers.{EmptyResponse, SelfEmploymentStatementResponse}

import scala.concurrent.{ExecutionContext, Future}

object SelfEmploymentStatementConnector {

  private lazy val baseUrl = AppContext.desUrl

  def create(nino: Nino, id: SourceId, accountingPeriod: Period, requestTimestamp: String)(implicit hc: HeaderCarrier, ec: ExecutionContext): Future[EmptyResponse] =
    httpPost[RequestDateTime, EmptyResponse](s"$baseUrl/income-store/nino/$nino/self-employments/$id/accounting-periods/${accountingPeriod.periodId}/statement",
      RequestDateTime(requestTimestamp), EmptyResponse)

  def get(nino: Nino, params: ObligationQueryParams)(implicit hc: HeaderCarrier, ec: ExecutionContext): Future[SelfEmploymentStatementResponse] = {
    val queryString = (params.from, params.to) match {
      case (None, None) => ""
      case (Some(f), Some(t)) => s"?from=$f&to=$t"
      case (Some(f), None) => s"?from=$f"
      case (None, Some(t)) => s"?to=$t"
    }
    httpGet[SelfEmploymentStatementResponse](baseUrl + s"/income-tax-self-assessment/obligation-data/$nino$queryString", SelfEmploymentStatementResponse)
  }

}
