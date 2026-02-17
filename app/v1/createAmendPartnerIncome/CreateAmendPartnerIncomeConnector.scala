package v1.createAmendPartnerIncome

import api.config.AppConfig
import api.connectors.DownstreamUri.HipUri
import api.connectors.httpparsers.StandardDownstreamHttpParser.*
import api.connectors.{BaseDownstreamConnector, DownstreamOutcome}
import uk.gov.hmrc.http.HeaderCarrier
import uk.gov.hmrc.http.client.HttpClientV2
import v1.createAmendPartnerIncome.model.request.CreateAmendPartnerIncomeRequestData

import javax.inject.{Inject, Singleton}
import scala.concurrent.{ExecutionContext, Future}

@Singleton
class CreateAmendPartnerIncomeConnector @Inject() (val http: HttpClientV2, val appConfig: AppConfig) extends BaseDownstreamConnector {
  
  def createAmendPartnerIncome(request: CreateAmendPartnerIncomeRequestData)(using
  hc: HeaderCarrier,
  ec: ExecutionContext,
  correlationId: String): Future[DownstreamOutcome[Unit]] = {
    
    import request.*
    
    val downstreamUri = HipUri[Unit](s"itsa/income-tax/v1/${taxYear.asTysDownstream}/income/partnerships/$nino")
    
    put(body, downstreamUri)
  }
}
