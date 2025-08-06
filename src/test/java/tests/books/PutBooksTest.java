package tests.books;

import base.TestBase;
import endpoints.BookEndpoints;
import io.restassured.response.Response;
import org.testng.annotations.Test;

import static org.testng.Assert.*;
import static specs.ResponseSpecs.*; 


public class PutBooksTest extends TestBase {

    @Test(description = "PUT /Books/9 happy path -> 200 & body echoes fields")
    public void put_happy200_id9() {
        String payload = """
            {
              "id": 9,
              "title": "string",
              "description": "string",
              "pageCount": 3849,
              "excerpt": "string",
              "publishDate": "1957-03-04T04:21:06.126Z"
            }
            """;

        Response resp = BookEndpoints.updateBookRaw(9, payload);
        resp.then().spec(ok200());

        var jp = resp.jsonPath();
        assertEquals((int) jp.getInt("id"), 9);
        assertEquals(jp.getString("title"), "string");
        assertEquals(jp.getString("description"), "string");
        assertEquals((int) jp.getInt("pageCount"), 3849);
        assertEquals(jp.getString("excerpt"), "string");
        assertEquals(jp.getString("publishDate"), "1957-03-04T04:21:06.126Z");
    }

    @Test(description = "PUT bad publishDate -> 400 + error on $.publishDate (contains)")
    public void put_badPublishDate_400_and_errorMessage_contains() {
        String payload = """
            {
              "title": "string",
              "description": "string",
              "pageCount": 3849,
              "excerpt": "string",
              "publishDate": "a"
            }
            """;

        Response resp = BookEndpoints.updateBookRaw(9, payload);
        resp.then().spec(badRequest400());

        String expected = "The JSON value could not be converted to System.DateTime. Path: $.publishDate";
        assertFirstErrorContains(resp, "$.publishDate", expected);
    }


    @Test(description = "PUT integer excerpt -> 400 + error on $.excerpt (contains)")
    public void put_excerptAsInteger_400_and_errorMessage_contains() {
        String payload = """
            {
              "title": "string",
              "description": "string",
              "pageCount": 3849,
              "excerpt": 1,
              "publishDate": "1957-03-04T04:21:06.126Z"
            }
            """;

        Response resp = BookEndpoints.updateBookRaw(9, payload);
        resp.then().spec(badRequest400());

        String expected = "The JSON value could not be converted to System.String. Path: $.excerpt";
        assertFirstErrorContains(resp, "$.excerpt", expected);
    }

    @Test(description = "PUT string pageCount -> 400 + error on $.pageCount (contains)")
    public void put_pageCountAsString_400_and_errorMessage_contains() {
        String payload = """
            {
              "title": "string",
              "description": "string",
              "pageCount": "a",
              "excerpt": "string",
              "publishDate": "1957-03-04T04:21:06.126Z"
            }
            """;

        Response resp = BookEndpoints.updateBookRaw(9, payload);
        resp.then().spec(badRequest400());

        String expected = "The JSON value could not be converted to System.Int32. Path: $.pageCount";
        assertFirstErrorContains(resp, "$.pageCount", expected);
    }

    @Test(description = "PUT integer description -> 400 + error on $.description (contains)")
    public void put_descriptionAsInteger_400_and_errorMessage_contains() {
        String payload = """
            {
              "title": "string",
              "description": 1,
              "pageCount": 3849,
              "excerpt": "string",
              "publishDate": "1957-03-04T04:21:06.126Z"
            }
            """;

        Response resp = BookEndpoints.updateBookRaw(9, payload);
        resp.then().spec(badRequest400());

        String expected = "The JSON value could not be converted to System.String. Path: $.description";
        assertFirstErrorContains(resp, "$.description", expected);
    }

    /* ----------------- helpers ----------------- */

    private static void assertFirstErrorContains(Response resp, String jsonPointer, String expectedSubstring) {
        String path = "errors.'" + jsonPointer + "'[0]";
        String actual = resp.jsonPath().getString(path);
        assertNotNull(actual, "Expected error message for key: " + jsonPointer);
        assertTrue(actual.contains(expectedSubstring),
            "Expected error for '" + jsonPointer + "' to contain:\n" + expectedSubstring + "\nbut got:\n" + actual);
    }
}
