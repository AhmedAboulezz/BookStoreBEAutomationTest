package tests.authors;

import base.TestBase;
import dataproviders.AuthorDataProviders;
import endpoints.AuthorEndpoints;
import io.restassured.response.Response;
import org.testng.annotations.Test;
import utils.helpers;

import static specs.ResponseSpecs.*;

public class DeleteAuthorsTest extends TestBase {

    @Test(
        dataProvider = "deleteAuthorNumericCases",
        dataProviderClass = AuthorDataProviders.class,
        description = "DELETE /Authors/{id} numeric id scenarios"
    )
    public void delete_numericCases(int id, int expectedStatus) {
        Response resp = AuthorEndpoints.deleteAuthor(id);
        switch (expectedStatus) {
            case 200 -> resp.then().spec(ok200());
            case 400 -> resp.then().spec(badRequest400());
            case 404 -> resp.then().spec(notFound404());
            default  -> throw new AssertionError("Unexpected expectedStatus: " + expectedStatus);
        }
    }

    @Test(
        dataProvider = "deleteAuthorStringCases",
        dataProviderClass = AuthorDataProviders.class,
        description = "DELETE /Authors/{id} string/overflow id scenarios -> 400 + errors.id assertion"
    )
    public void delete_stringCases(String idAsString, String expectedErrorEquals) {
        Response resp = AuthorEndpoints.deleteAuthorRawPath(idAsString);
        resp.then().spec(badRequest400());
        helpers.assertErrorsIdEquals(resp, expectedErrorEquals);
    }

    @Test(description = "DELETE /Authors (no id) -> 405 Method Not Allowed")
    public void delete_withoutId_405() {
        Response resp = AuthorEndpoints.deleteAuthorsCollection();
        resp.then().spec(methodNotAllowed405());
    }
}
