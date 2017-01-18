package uk.gov.hmrc.selfassessmentapi.resources

import org.joda.time.LocalDate
import play.api.libs.json.{JsValue, Json}

object Jsons {

  object Errors {

    private def error(error: (String, String)) = {
      s"""
         |    {
         |      "code": "${error._1}",
         |      "path": "${error._2}"
         |    }
         """.stripMargin
    }

    val notFound: String =
      s"""
         |{
         |  "code": "NOT_FOUND",
         |  "message": "Resource was not found"
         |}
       """.stripMargin

    def invalidRequest(errors: (String, String)*): String = {
      s"""
         |{
         |  "code": "INVALID_REQUEST",
         |  "message": "Invalid request",
         |  "errors": [
         |    ${errors.map { error }.mkString(",")}
         |  ]
         |}
         """.stripMargin
    }

    def businessError(errors: (String, String)*): String = {
      s"""
         |{
         |  "code": "BUSINESS_ERROR",
         |  "message": "Business validation error",
         |  "errors": [
         |    ${errors.map { error }.mkString(",")}
         |  ]
         |}
         """.stripMargin
    }
  }

  object Banks {
    def apply(accountName: String = "Savings Account", foreign: Boolean = false): JsValue = {
      Json.parse(
        s"""
           |{
           |  "accountName": "$accountName"
           |}
         """.stripMargin)
    }
  }

  object Properties {
    def apply(accountingType: String = "CASH"): JsValue = {
      Json.parse(
        s"""
           |{
           |  "accountingType": "$accountingType"
           |}
         """.stripMargin)
    }

    def period(fromDate: Option[String] = None,
               toDate: Option[String] = None,
               rentIncome: BigDecimal = 0,
               rentIncomeTaxDeducted: BigDecimal = 0,
               premiumsOfLeaseGrant: Option[BigDecimal] = None,
               reversePremiums: BigDecimal = 0,
               premisesRunningCosts: BigDecimal = 0,
               repairsAndMaintenance: BigDecimal = 0,
               financialCosts: BigDecimal = 0,
               professionalFees: BigDecimal = 0,
               costOfServices: BigDecimal = 0,
               otherCost: BigDecimal = 0): JsValue = {

      val from =
        fromDate.map { date =>
          s"""
             | "from": "$date",
         """.stripMargin
        }.getOrElse("")

      val to =
        toDate.map { date =>
          s"""
             | "to": "$date",
         """.stripMargin
        }.getOrElse("")

      Json.parse(
        s"""
           |{
           |  $from
           |  $to
           |  "incomes": {
           |    "rentIncome": { "amount": $rentIncome, "taxDeducted": $rentIncomeTaxDeducted },
           |    ${premiumsOfLeaseGrant.map(income => s""" "premiumsOfLeaseGrant": { "amount": $income },""").getOrElse("")}
           |    "reversePremiums": { "amount": $reversePremiums }
           |  },
           |  "expenses": {
           |    "premisesRunningCosts": { "amount": $premisesRunningCosts },
           |    "repairsAndMaintenance": { "amount": $repairsAndMaintenance },
           |    "financialCosts": { "amount": $financialCosts },
           |    "professionalFees": { "amount": $professionalFees },
           |    "costOfServices": { "amount": $costOfServices },
           |    "other": { "amount": $otherCost }
           |  }
           |}
       """.stripMargin)
    }

    def periodSummary(dates: (String, String)*): JsValue = {
      val nestedDates = dates.map { date =>
        s"""
           |{
           |  "from": "${date._1}",
           |  "to": "${date._2}"
           |}
           """.stripMargin
      }.mkString(",")

      Json.parse(
        s"""
           |[
           |  $nestedDates
           |]
         """.stripMargin
      )
    }

    def fhlAnnualSummary(annualInvestmentAllowance: BigDecimal = 0.0,
                         otherCapitalAllowance: BigDecimal = 0.0,
                         lossBroughtForward: BigDecimal = 0.0,
                         privateUseAdjustment: BigDecimal = 0.0,
                         balancingCharge: BigDecimal = 0.0): JsValue = {
      Json.parse(s"""
                    |{
                    |  "allowances": {
                    |    "annualInvestmentAllowance": $annualInvestmentAllowance,
                    |    "otherCapitalAllowance": $otherCapitalAllowance
                    |  },
                    |  "adjustments": {
                    |   "lossBroughtForward": $lossBroughtForward,
                    |   "privateUseAdjustment": $privateUseAdjustment,
                    |   "balancingCharge": $balancingCharge
                    |  }
                    |}
    """.stripMargin)
    }

    def otherAnnualSummary(annualInvestmentAllowance: BigDecimal = 0.0,
                          businessPremisesRenovationAllowance : BigDecimal = 0.0,
                          otherCapitalAllowance: BigDecimal = 0.0,
                           zeroEmissionsGoodsVehicleAllowance: BigDecimal = 0.0,
                          costOfReplacingDomesticItems: BigDecimal = 0.0,
                          lossBroughtForward: BigDecimal = 0.0,
                          privateUseAdjustment: BigDecimal = 0.0,
                          balancingCharge: BigDecimal = 0.0): JsValue = {
      Json.parse(s"""
                    |{
                    |  "allowances": {
                    |    "annualInvestmentAllowance": $annualInvestmentAllowance,
                    |    "businessPremisesRenovationAllowance": $businessPremisesRenovationAllowance,
                    |    "otherCapitalAllowance": $otherCapitalAllowance,
                    |    "costOfReplacingDomesticItems": $costOfReplacingDomesticItems,
                    |    "zeroEmissionsGoodsVehicleAllowance": $zeroEmissionsGoodsVehicleAllowance
                    |  },
                    |  "adjustments": {
                    |   "lossBroughtForward": $lossBroughtForward,
                    |   "privateUseAdjustment": $privateUseAdjustment,
                    |   "balancingCharge": $balancingCharge
                    |  }
                    |}
    """.stripMargin)
    }

  }

  object SelfEmployment {
    def apply(accPeriodStart: String = "2017-04-06", accPeriodEnd: String = "2018-04-05", accountingType: String = "CASH",
                       commencementDate: String = s"${LocalDate.now.minusDays(1)}"): JsValue = {
      Json.parse(
        s"""
           |{
           |  "accountingPeriod": {
           |    "start": "$accPeriodStart",
           |    "end": "$accPeriodEnd"
           |  },
           |  "accountingType": "$accountingType",
           |  "commencementDate": "$commencementDate"
           |}
         """.stripMargin)
    }

    def annualSummary(annualInvestmentAllowance: BigDecimal = 500.25, capitalAllowanceMainPool: BigDecimal = 500.25,
                      capitalAllowanceSpecialRatePool: BigDecimal = 500.25, businessPremisesRenovationAllowance: BigDecimal = 500.25,
                      enhancedCapitalAllowance: BigDecimal = 500.25, allowanceOnSales: BigDecimal = 500.25,
                      zeroEmissionGoodsVehicleAllowance: BigDecimal = 500.25,
                      includedNonTaxableProfits: BigDecimal = 500.25, basisAdjustment: BigDecimal = -500.25,
                      overlapReliefUsed: BigDecimal = 500.25, accountingAdjustment: BigDecimal = 500.25,
                      averagingAdjustment: BigDecimal = -500.25, lossBroughtForward: BigDecimal = 500.25,
                      outstandingBusinessIncome: BigDecimal = 500.25, balancingChargeBPRA: BigDecimal = 500.25,
                      balancingChargeOther: BigDecimal = 500.25, goodsAndServicesOwnUse: BigDecimal = 500.25): JsValue = {
      Json.parse(
        s"""
           |{
           |  "allowances": {
           |    "annualInvestmentAllowance": $annualInvestmentAllowance,
           |    "capitalAllowanceMainPool": $capitalAllowanceMainPool,
           |    "capitalAllowanceSpecialRatePool": $capitalAllowanceSpecialRatePool,
           |    "businessPremisesRenovationAllowance": $businessPremisesRenovationAllowance,
           |    "enhancedCapitalAllowance": $enhancedCapitalAllowance,
           |    "allowanceOnSales": $allowanceOnSales,
           |    "zeroEmissionGoodsVehicleAllowance": $zeroEmissionGoodsVehicleAllowance
           |  },
           |  "adjustments": {
           |    "includedNonTaxableProfits": $includedNonTaxableProfits,
           |    "basisAdjustment": $basisAdjustment,
           |    "overlapReliefUsed": $overlapReliefUsed,
           |    "accountingAdjustment": $accountingAdjustment,
           |    "averagingAdjustment": $averagingAdjustment,
           |    "lossBroughtForward": $lossBroughtForward,
           |    "outstandingBusinessIncome": $outstandingBusinessIncome,
           |    "balancingChargeBPRA": $balancingChargeBPRA,
           |    "balancingChargeOther": $balancingChargeOther,
           |    "goodsAndServicesOwnUse": $goodsAndServicesOwnUse
           |  }
           |}
       """.stripMargin)
    }



    def period(fromDate: Option[String] = None, toDate: Option[String] = None,
                             turnover: BigDecimal = 0, otherIncome: BigDecimal = 0,
                             costOfGoodsBought: (BigDecimal, BigDecimal) = (0, 0), cisPaymentsToSubcontractors: (BigDecimal, BigDecimal) = (0, 0),
                             staffCosts: (BigDecimal, BigDecimal) = (0, 0), travelCosts: (BigDecimal, BigDecimal) = (0, 0),
                             premisesRunningCosts: (BigDecimal, BigDecimal) = (0, 0), maintenanceCosts: (BigDecimal, BigDecimal) = (0, 0),
                             adminCosts: (BigDecimal, BigDecimal) = (0, 0), advertisingCosts: (BigDecimal, BigDecimal) = (0, 0),
                             interest: (BigDecimal, BigDecimal) = (0, 0), financialCharges: (BigDecimal, BigDecimal) = (0, 0),
                             badDebt: (BigDecimal, BigDecimal) = (0, 0), professionalFees: (BigDecimal, BigDecimal) = (0, 0),
                             depreciation: (BigDecimal, BigDecimal) = (0, 0), otherExpenses: (BigDecimal, BigDecimal) = (0, 0)): JsValue = {

      val from =
        fromDate.map { date =>
          s"""
             | "from": "$date",
         """.stripMargin
        }.getOrElse("")

      val to =
        toDate.map { date =>
          s"""
             | "to": "$date",
         """.stripMargin
        }.getOrElse("")

      Json.parse(
        s"""
           |{
           |  $from
           |  $to
           |  "incomes": {
           |    "turnover": { "amount": $turnover },
           |    "other": { "amount": $otherIncome }
           |  },
           |  "expenses": {
           |    "costOfGoodsBought": { "amount": ${costOfGoodsBought._1}, "disallowableAmount": ${costOfGoodsBought._2} },
           |    "cisPaymentsToSubcontractors": { "amount": ${cisPaymentsToSubcontractors._1}, "disallowableAmount": ${cisPaymentsToSubcontractors._2} },
           |    "staffCosts": { "amount": ${staffCosts._1}, "disallowableAmount": ${staffCosts._2} },
           |    "travelCosts": { "amount": ${travelCosts._1}, "disallowableAmount": ${travelCosts._2} },
           |    "premisesRunningCosts": { "amount": ${premisesRunningCosts._1}, "disallowableAmount": ${premisesRunningCosts._2} },
           |    "maintenanceCosts": { "amount": ${maintenanceCosts._1}, "disallowableAmount": ${maintenanceCosts._2} },
           |    "adminCosts": { "amount": ${adminCosts._1}, "disallowableAmount": ${adminCosts._2} },
           |    "advertisingCosts": { "amount": ${advertisingCosts._1}, "disallowableAmount": ${advertisingCosts._2} },
           |    "interest": { "amount": ${interest._1}, "disallowableAmount": ${interest._2} },
           |    "financialCharges": { "amount": ${financialCharges._1}, "disallowableAmount": ${financialCharges._2} },
           |    "badDebt": { "amount": ${badDebt._1}, "disallowableAmount": ${badDebt._2} },
           |    "professionalFees": { "amount": ${professionalFees._1}, "disallowableAmount": ${professionalFees._2} },
           |    "depreciation": { "amount": ${depreciation._1}, "disallowableAmount": ${depreciation._2} },
           |    "other": { "amount": ${otherExpenses._1}, "disallowableAmount": ${otherExpenses._2} }
           |  }
           |}
       """.stripMargin)
    }

  }

  object Dividends {
    def apply(amount: BigDecimal): JsValue = {
      Json.parse(
        s"""
           |{
           |  "ukDividends": $amount
           |}
         """.stripMargin)
    }
  }
}
