package tests.authors;

import base.TestBase;
import endpoints.AuthorEndpoints;
import io.restassured.response.Response;
import org.testng.annotations.Test;

import static org.testng.Assert.assertEquals;
import static specs.ResponseSpecs.*;

public class DeleteAuthorsTest extends TestBase {

    private static void assertErrorsIdEquals(Response resp, String expected) {
        String actual = resp.jsonPath().getString("errors.id[0]");
        assertEquals(actual, expected, "errors.id[0] mismatch");
    }

    @Test(description = "DELETE /Authors/1 -> 200")
    public void delete_id1_200() {
        Response resp = AuthorEndpoints.deleteAuthor(1);
        resp.then().spec(ok200());
    }

    @Test(description = "DELETE /Authors/-1 -> 400")
    public void delete_negativeId_400() {
        Response resp = AuthorEndpoints.deleteAuthor(-1);
        resp.then().spec(badRequest400());
    }

    @Test(description = "DELETE /Authors/abc -> 400 and errors.id asserted")
    public void delete_stringId_400_and_errorsId() {
        String bad = "abc";
        Response resp = AuthorEndpoints.deleteAuthorRawPath(bad);
        resp.then().spec(badRequest400());
        assertErrorsIdEquals(resp, "The value '" + bad + "' is not valid.");
    }

 
    @Test(description = "DELETE /Authors (no id) -> 405")
    public void delete_withoutId_405() {
        Response resp = AuthorEndpoints.deleteAuthorsCollection();
        resp.then().spec(methodNotAllowed405());
    }

    @Test(description = "DELETE /Authors/0 -> 404")
    public void delete_zeroId_404() {
        Response resp = AuthorEndpoints.deleteAuthor(0);
        resp.then().spec(notFound404());
    }

    @Test(description = "DELETE /Authors/999999999999999999999 -> 400 and errors.id asserted")
    public void delete_hugeNumericString_400_and_errorsId() {
        String huge = "999999999999999999999";
        Response resp = AuthorEndpoints.deleteAuthorRawPath(huge);
        resp.then().spec(badRequest400());
        assertErrorsIdEquals(resp, "The value '" + huge + "' is not valid.");
    }
}
