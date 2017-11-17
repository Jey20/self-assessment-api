package uk.gov.hmrc

class ZapRunner extends ZapTest {

  /**
    * zapBaseUrl is a required field - you'll need to set it in this file, for your project to compile.
    * It will rarely need to be changed. We've included it as an overridable field
    * for flexibility and just in case.
    */
  override val zapBaseUrl: String = "http://localhost:11000"

  /**
    * testUrl is a required field - you'll need to set it in this file, for your project to compile.
    * It needs to be the URL of the start page of your application (not just localhost:port).
    */
  override val testUrl: String = "http://localhost:19001/ni"

  /**
    * desiredTechnologyNames is not a required field. We recommend you don't change this
    * field, as we've made basic choices for the platform. We made it overridable just in case
    * your service differs from the standards of the Platform.
    *
    * The technologies that you put here will limit the amount of checks that ZAP will do to
    * just the technologies that are relevant. The default technologies are set to
    * "OS,OS.Linux,Language,Language.Xml,SCM,SCM.Git".
    */
  //override val desiredTechnologyNames: String = ""

  /**
    * routesToBeIgnoredFromContext is not a required field. You may set this if you have any routes
    * that are part of your application, but you do not want tested. For example, if you had any
    * test-only routes, you could force Zap not to test them by adding them in here as a regex.
    */
  //override val routeToBeIgnoredFromContext: String = "xxx"

  /**
    * If, when you run the Zap tests, you find alerts that you have investigated and don't see as a problem
    * you can filter them out using this code, on the cweid and the url that the alert was found on.
    * The CWE ID is a Common Weakness Enumeration (http://cwe.mitre.org/data/index.html), you can
    * find this by looking at the alert output from your tests.
    * As part of the trial, please try
    * filtering out a few alerts and seeing if this functionality works for you.
    * Below you can see an example of how this might work.
    */
  val alertToBeIgnored1: ZapAlertFilter = ZapAlertFilter(cweid = "16", url = "xxx")
  override val alertsToIgnore: List[ZapAlertFilter] = List(alertToBeIgnored1)

}
