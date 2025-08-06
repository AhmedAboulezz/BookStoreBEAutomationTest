package tests.books;

import base.TestBase;
import endpoints.BookEndpoints;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import models.Book;
import org.testng.annotations.Test;

import java.util.List;
import java.util.Map;

import static org.testng.Assert.*;
import static specs.ResponseSpecs.*;

public class GetBookByIdTest extends TestBase {

    @Test(description = "GET /Books/1 -> 200 and full book object with non-null fields")
    public void getBookById_happy() {
        Response resp = BookEndpoints.getBookByIdJson(1);
        resp.then().spec(ok200());

        Book b = resp.as(Book.class);
        assertNotNull(b, "Book response must not be null");
        assertEquals(b.getId(), Integer.valueOf(1), "Expected id=1");
        assertNotNull(b.getTitle(), "title must not be null");
        assertNotNull(b.getDescription(), "description must not be null");
        assertNotNull(b.getExcerpt(), "excerpt must not be null");
        assertNotNull(b.getPageCount(), "pageCount must not be null");
        assertNotNull(b.getPublishDate(), "publishDate must not be null");
    }

    @Test(description = "GET /Books/1 Content-Type contains API version v=1.0")
    public void getBookById_contentTypeHasVersion() {
        Response resp = BookEndpoints.getBookByIdJson(1);
        resp.then().spec(ok200());
        String contentType = resp.getHeader("Content-Type");
        assertNotNull(contentType, "Content-Type header should be present");
        assertTrue(contentType.contains("v=1.0"),
                "Expected Content-Type to contain API version v=1.0, but was: " + contentType);
    }

    @Test(description = "GET /Books/a -> 400 with errors.id containing not-valid message")
    public void getBookById_nonNumeric_returns400() {
        Response resp = BookEndpoints.getBookByIdRaw("a");
        resp.then().spec(badRequest400());
        assertFirstErrorContains(resp, "id", "The value 'a' is not valid.");
    }

    @Test(description = "GET /Books/-1 -> 404 Not Found")
    public void getBookById_negative_returns404() {
        Response resp = BookEndpoints.getBookByIdJson(-1);
        resp.then().spec(notFound404());
    }

    @Test(description = "GET /Books/201 -> 404 Not Found for non-existent book")
    public void getBookById_notExisting_returns404() {
        Response resp = BookEndpoints.getBookByIdJson(201);
        resp.then().spec(notFound404());
    }

    // ---------- helpers ----------

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
