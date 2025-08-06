package tests.authors;

import base.TestBase;
import endpoints.AuthorEndpoints;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import models.Author;
import org.testng.annotations.Test;

import java.util.List;
import java.util.Map;

import static org.testng.Assert.*;
import static specs.ResponseSpecs.*; 

public class GetAuthorByIdTest extends TestBase {

    @Test(description = "GET /Authors/1 -> 200 and full author object with non-null fields")
    public void getAuthorById_happy() {
        Response resp = AuthorEndpoints.getAuthorByIdJson(1);
        resp.then().spec(ok200());

        Author a = resp.as(Author.class);
        assertNotNull(a, "Author response must not be null");
        assertEquals(a.getId(), Integer.valueOf(1), "Expected id=1");
        assertNotNull(a.getIdBook(), "idBook must not be null");
        assertNotNull(a.getFirstName(), "firstName must not be null");
        assertNotNull(a.getLastName(), "lastName must not be null");
    }

    @Test(description = "GET /Authors/1 Content-Type contains API version v=1.0")
    public void getAuthorById_contentTypeHasVersion() {
        Response resp = AuthorEndpoints.getAuthorByIdJson(1);
        resp.then().spec(ok200());

        String contentType = resp.getHeader("Content-Type");
        assertNotNull(contentType, "Content-Type header should be present");
        assertTrue(contentType.contains("v=1.0"),
                "Expected Content-Type to contain API version v=1.0, but was: " + contentType);
    }

    @Test(description = "GET /Authors/a -> 400 with errors.id containing not-valid message")
    public void getAuthorById_nonNumeric_returns400() {
        Response resp = AuthorEndpoints.getAuthorByIdRaw("a");
        resp.then().spec(badRequest400());

        assertFirstErrorContains(resp, "id", "The value 'a' is not valid.");
    }

    @Test(description = "GET /Authors/-1 -> 404 Not Found")
    public void getAuthorById_negative_returns404() {
        Response resp = AuthorEndpoints.getAuthorByIdJson(-1);
        resp.then().spec(notFound404());
    }

    @Test(description = "GET /Authors/10000 -> 404 Not Found (non-existent)")
    public void getAuthorById_notExisting_returns404() {
        Response resp = AuthorEndpoints.getAuthorByIdJson(10000);
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
