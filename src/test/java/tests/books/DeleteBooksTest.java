package tests.books;

import endpoints.BookEndpoints;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import org.testng.annotations.Test;

import base.TestBase;

import static org.testng.Assert.*;

/**
 * DELETE /api/v1/Books/{id} strict scenarios requested:
 * 1) Happy path delete id=1 -> 200
 * 2) Delete negative id -> 400
 * 3) Delete with string id -> 400 + assert errors.id
 * 4) Delete without id -> 405
 * 5) Delete id=0 -> 404 (suggested)
 * 6) Delete huge numeric string -> 400 + assert errors.id
 */
public class DeleteBooksTest extends TestBase {

    private static void assertErrorsIdEquals(Response resp, String expected) {
        JsonPath jp = resp.jsonPath();
        String actual = jp.getString("errors.id[0]");
        assertEquals(actual, expected, "errors.id[0] mismatch");
    }
    
    @Test
    public void verify_extent_report_ahmaaad() {
        logInfo("This is an INFO log from the sanity test.");
        logPass("Manual PASS logged ✅");
    }


    /** 1) Happy Scenario .. delete book id = 1 , assert 200 */
    @Test(description = "DELETE id=1 -> 200")
    public void delete_id1_200() {
        Response resp = BookEndpoints.deleteBook(1);
        logInfo("Test started");
        logPass("All good here ✅");
        assertEquals(resp.getStatusCode(), 200, "Expected 200 for deleting id=1");
        // No response body asserted by requirement.
    }

    /** 2) Delete book -ve number , assert 400 */
    @Test(description = "DELETE negative id -> 400")
    public void delete_negativeId_400() {
        Response resp = BookEndpoints.deleteBook(-1);
        assertEquals(resp.getStatusCode(), 400, "Expected 400 for negative id");
    }

    /**
     * 3) Delete book with string value instead of integer assert 400 and assert only errors.id
     * Expected error shape:
     *   "errors": { "id": [ "The value 'abc' is not valid." ] }
     */
    @Test(description = "DELETE with string id -> 400 and errors.id asserted")
    public void delete_stringId_400_and_errorsId() {
        String bad = "abc";
        Response resp = BookEndpoints.deleteBookRawPath(bad);
        assertEquals(resp.getStatusCode(), 400, "Expected 400 for non-integer id");
        assertErrorsIdEquals(resp, "The value '" + bad + "' is not valid.");
    }

    /** 4) without id , 405 is expected */
    @Test(description = "DELETE without id -> 405")
    public void delete_withoutId_405() {
        Response resp = BookEndpoints.deleteBooksCollection();
        assertEquals(resp.getStatusCode(), 405, "Expected 405 for DELETE /Books without id");
    }

    /** 5) delete with id = 0 , should fail , suggest 404 */
    @Test(description = "DELETE id=0 -> 404 (suggested)")
    public void delete_zeroId_404() {
        Response resp = BookEndpoints.deleteBook(0);
        assertEquals(resp.getStatusCode(), 404, "Expected 404 for id=0");
    }

    /**
     * 6) delete 999999999999999999999 -> 400 and assert errors.id
     * Expected:
     * "errors": { "id": [ "The value '999999999999999999999' is not valid." ] }
     */
    @Test(description = "DELETE huge numeric string -> 400 and errors.id asserted")
    public void delete_hugeNumericString_400_and_errorsId() {
        String huge = "999999999999999999999";
        Response resp = BookEndpoints.deleteBookRawPath(huge);
        assertEquals(resp.getStatusCode(), 400, "Expected 400 for huge numeric string");
        assertErrorsIdEquals(resp, "The value '" + huge + "' is not valid.");
    }
}
