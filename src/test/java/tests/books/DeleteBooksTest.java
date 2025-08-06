package tests.books;

import base.TestBase;
import dataproviders.BookDataProviders;
import endpoints.BookEndpoints;
import io.restassured.response.Response;
import org.testng.annotations.Test;
import utils.helpers;

import static specs.ResponseSpecs.*;

public class DeleteBooksTest extends TestBase {

    @Test(
        dataProvider = "deleteBookNumericCases",
        dataProviderClass = BookDataProviders.class,
        description = "DELETE /Books/{id} numeric id scenarios"
    )
    public void delete_numericCases(int id, int expectedStatus) {
        Response resp = BookEndpoints.deleteBook(id);
        switch (expectedStatus) {
            case 200 -> resp.then().spec(ok200());
            case 400 -> resp.then().spec(badRequest400());
            case 404 -> resp.then().spec(notFound404());
            default  -> throw new AssertionError("Unexpected expectedStatus: " + expectedStatus);
        }
    }

    @Test(
        dataProvider = "deleteBookStringCases",
        dataProviderClass = BookDataProviders.class,
        description = "DELETE /Books/{id} string/overflow id scenarios -> 400 + errors.id"
    )
    public void delete_stringCases(String idAsString, String expectedErrorEquals) {
        Response resp = BookEndpoints.deleteBookRawPath(idAsString);
        resp.then().spec(badRequest400());
        helpers.assertErrorsIdEquals(resp, expectedErrorEquals);
    }

    @Test(description = "DELETE /Books (no id) -> 405 Method Not Allowed")
    public void delete_withoutId_405() {
        Response resp = BookEndpoints.deleteBooksCollection();
        resp.then().spec(methodNotAllowed405());
    }
}
