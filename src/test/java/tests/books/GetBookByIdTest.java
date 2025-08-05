package tests.books;

import base.TestBase;
import endpoints.BookEndpoints;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import models.Book;
import org.testng.annotations.Test;
import specs.ResponseSpecs;

import java.util.List;
import java.util.Map;

import static org.testng.Assert.*;

/**
 * GET /api/v1/Books/{id} – happy & edge cases
 */
public class GetBookByIdTest extends TestBase {

    /**
     * 1) Happy path:
     * curl --location 'https://fakerestapi.azurewebsites.net/api/v1/Books/1' --header 'Accept: text/plain; v=1.0'
     * Validate status 200 and all fields not null; id == 1.
     */
    @Test(description = "GET /Books/1 returns 200 and full book object with non-null fields")
    public void getBookById_happy() {
        Response resp = BookEndpoints.getBookByIdJson(1);
        resp.then().spec(ResponseSpecs.ok200());

        Book b = resp.as(Book.class);
        assertNotNull(b, "Book response must not be null");
        assertEquals(b.getId(), Integer.valueOf(1), "Expected id=1");
        assertNotNull(b.getTitle(), "title must not be null");
        assertNotNull(b.getDescription(), "description must not be null");
        assertNotNull(b.getExcerpt(), "excerpt must not be null");
        assertNotNull(b.getPageCount(), "pageCount must not be null");
        assertNotNull(b.getPublishDate(), "publishDate must not be null");
    }

    /**
     * 2) Non-numeric id ("a") => 400 + errors.id contains "The value 'a' is not valid."
     */
    @Test(description = "GET /Books/a returns 400 with errors.id containing not-valid message")
    public void getBookById_nonNumeric_returns400() {
        Response resp = BookEndpoints.getBookByIdRaw("a");
        resp.then().spec(ResponseSpecs.badRequest400());

        assertFirstErrorContains(resp, "id", "The value 'a' is not valid.");
    }

    /**
     * 3) Negative id => 404 (Not Found) as per your expectation.
     */
    @Test(description = "GET /Books/-1 returns 404 Not Found")
    public void getBookById_negative_returns404() {
        Response resp = BookEndpoints.getBookByIdJson(-1);
        resp.then().spec(ResponseSpecs.notFound404());
    }

    /**
     * 4) Non-existent id (e.g., 201) => 404 (Not Found).
     */
    @Test(description = "GET /Books/201 returns 404 Not Found for non-existent book")
    public void getBookById_notExisting_returns404() {
        Response resp = BookEndpoints.getBookByIdJson(201);
        resp.then().spec(ResponseSpecs.notFound404());
    }

    // ---------- helpers ----------

    /**
     * Assert that the first error under 'errors.<key>' contains expectedSubstring.
     * Expects a validation error payload like:
     * {
     *   "errors": { "<key>": [ "message1", ... ] }
     * }
     */
    private static void assertFirstErrorContains(Response resp, String key, String expectedSubstring) {
        JsonPath jp = resp.jsonPath();
        Map<String, List<String>> errors = jp.getMap("errors");
        assertNotNull(errors, "'errors' object should be present in the response");
        List<String> msgs = errors.get(key);
        assertNotNull(msgs, "'errors." + key + "' should be present");
        assertFalse(msgs.isEmpty(), "'errors." + key + "' should have at least one message");
        String first = msgs.get(0);
        assertTrue(first.contains(expectedSubstring),
                "Expected first error for '" + key + "' to contain: " + expectedSubstring + " but was: " + first);
    }
}
