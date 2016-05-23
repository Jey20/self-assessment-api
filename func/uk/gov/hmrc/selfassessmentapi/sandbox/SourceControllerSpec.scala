package uk.gov.hmrc.selfassessmentapi.sandbox

import java.util.UUID

import uk.gov.hmrc.selfassessmentapi.domain.SourceTypes
import uk.gov.hmrc.support.BaseFunctionalSpec

class SourceControllerSpec extends BaseFunctionalSpec {

  val sourceId = UUID.randomUUID().toString

  "Sandbox Self source" should {

    "return a 201 response with links to newly created source" in {
      SourceTypes.types.foreach { sourceType =>
        val resp = given()
          .userIsAuthorisedForTheResource(saUtr)
          .when()
          .post(s"/sandbox/$saUtr/$taxYear/${sourceType.name}", Some(sourceType.example))
          .thenAssertThat()
          .statusIs(201)
          .contentTypeIsHalJson()
          .bodyHasLink("self", s"/self-assessment/$saUtr/$taxYear/${sourceType.name}/.+".r)

        sourceType.summaryTypes.foreach { summaryType =>
          resp.bodyHasLink(summaryType.name, s"/self-assessment/$saUtr/$taxYear/${sourceType.name}/.+/${summaryType.name}".r)
        }
      }
    }

    "return a valid response containing an existing source" in {
      SourceTypes.types.foreach { sourceType =>
        val resp = given()
          .userIsAuthorisedForTheResource(saUtr)
          .when()
          .get(s"/sandbox/$saUtr/$taxYear/${sourceType.name}/$sourceId")
          .thenAssertThat()
          .statusIs(200)
          .contentTypeIsHalJson()
          .bodyHasLink("self", s"/self-assessment/$saUtr/$taxYear/${sourceType.name}/$sourceId")

        sourceType.summaryTypes.foreach { summaryType =>
          resp.bodyHasLink(summaryType.name, s"/self-assessment/$saUtr/$taxYear/${sourceType.name}/$sourceId/${summaryType.name}".r)
        }
      }
    }

    "return a valid response when retrieving list of sources" in {
      SourceTypes.types.foreach { sourceType =>
        given()
          .when()
          .get(s"/sandbox/$saUtr/$taxYear/${sourceType.name}")
          .thenAssertThat()
          .statusIs(200)
          .contentTypeIsHalJson()
          .bodyHasLink("self", s"/self-assessment/$saUtr/$taxYear/${sourceType.name}")
          .bodyHasPath(s"""_embedded \\ ${sourceType.name}(0) \\ _links \\ self \\ href""", s"/self-assessment/$saUtr/$taxYear/${sourceType.name}/.+".r)
          .bodyHasPath(s"""_embedded \\ ${sourceType.name}(1) \\ _links \\ self \\ href""", s"/self-assessment/$saUtr/$taxYear/${sourceType.name}/.+".r)
          .bodyHasPath(s"""_embedded \\ ${sourceType.name}(2) \\ _links \\ self \\ href""", s"/self-assessment/$saUtr/$taxYear/${sourceType.name}/.+".r)
      }
    }

    "return 200 and a valid response when an existing source is modified" in {
      SourceTypes.types.foreach { sourceType =>
        val resp = given()
          .userIsAuthorisedForTheResource(saUtr)
          .when()
          .put(s"/sandbox/$saUtr/$taxYear/${sourceType.name}/$sourceId", Some(sourceType.example))
          .thenAssertThat()
          .statusIs(200)
          .contentTypeIsHalJson()
          .bodyHasLink("self", s"/self-assessment/$saUtr/$taxYear/${sourceType.name}/$sourceId".r)

        sourceType.summaryTypes.foreach { summaryType =>
          resp.bodyHasLink(summaryType.name, s"/self-assessment/$saUtr/$taxYear/${sourceType.name}/$sourceId/${summaryType.name}".r)
        }
      }
    }

    "return 204 response when an existing source is deleted" in {
      SourceTypes.types.foreach { sourceType =>
        given()
          .when()
          .delete(s"/sandbox/$saUtr/$taxYear/${sourceType.name}/$sourceId")
          .thenAssertThat()
          .statusIs(204)
      }
    }
  }
}