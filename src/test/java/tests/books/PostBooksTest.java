package tests.books;

import base.TestBase;
import endpoints.BookEndpoints;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import org.testng.annotations.Test;

import static org.testng.Assert.*;
import static specs.ResponseSpecs.*;

public class PostBooksTest extends TestBase {

    private void assertFirstErrorContains(Response resp, String jsonPointer, String expectedSubstring) {
        String path = "errors.'" + jsonPointer + "'[0]";
        String actual = resp.jsonPath().getString(path);
        assertNotNull(actual, "Expected error message for key: " + jsonPointer);
        assertTrue(actual.contains(expectedSubstring),
            "Expected error for '" + jsonPointer + "' to contain:\n" + expectedSubstring + "\nbut got:\n" + actual);
    }

    @Test(description = "POST without id -> 400")
    public void post_withoutId_400() {
        String payload = """
            {
              "title": "string",
              "description": "string",
              "pageCount": 3849,
              "excerpt": "string",
              "publishDate": "1957-03-04T04:21:06.126Z"
            }
            """;

        Response resp = BookEndpoints.createRawBook(payload);
        resp.then().spec(badRequest400());
    }


    @Test(description = "POST bad publishDate -> 400 + error on $.publishDate")
    public void post_badPublishDate_400_and_errorMessage() {
        String payload = """
            {
              "id": 3842,
              "title": "string",
              "description": "string",
              "pageCount": 3849,
              "excerpt": "string",
              "publishDate": "not-a-date"
            }
            """;

        Response resp = BookEndpoints.createRawBook(payload);
        resp.then().spec(badRequest400());

        String expected = "The JSON value could not be converted to System.DateTime. Path: $.publishDate";
        assertFirstErrorContains(resp, "$.publishDate", expected);
    }


    @Test(description = "POST happy path (no id) -> 200 and same fields echoed")
    public void post_happy_noId_200_echoedFields() {
        String title = "string";
        String desc = "string";
        int pageCount = 3849;
        String excerpt = "string";
        String publishDate = "1957-03-04T04:21:06.126Z";

        String payload = """
            {
              "title": "%s",
              "description": "%s",
              "pageCount": %d,
              "excerpt": "%s",
              "publishDate": "%s"
            }
            """.formatted(title, desc, pageCount, excerpt, publishDate);

        Response resp = BookEndpoints.createRawBook(payload);
        resp.then().spec(ok200());

        JsonPath jp = resp.jsonPath();
        assertEquals(jp.getString("title"), title);
        assertEquals(jp.getString("description"), desc);
        assertEquals((Integer) jp.getInt("pageCount"), Integer.valueOf(pageCount));
        assertEquals(jp.getString("excerpt"), excerpt);
        assertEquals(jp.getString("publishDate"), publishDate);
    }

    @Test(description = "POST excerpt as integer -> 400 + error on $.excerpt")
    public void post_excerptAsInteger_400_and_errorMessage() {
        String payload = """
            {
              "id": 3842,
              "title": "string",
              "description": "string",
              "pageCount": 3849,
              "excerpt": 123,
              "publishDate": "1957-03-04T04:21:06.126Z"
            }
            """;

        Response resp = BookEndpoints.createRawBook(payload);
        resp.then().spec(badRequest400());

        String expected = "The JSON value could not be converted to System.String. Path: $.excerpt";
        assertFirstErrorContains(resp, "$.excerpt", expected);
    }

    @Test(description = "POST pageCount as string -> 400 + error on $.pageCount")
    public void post_pageCountAsString_400_and_errorMessage() {
        String payload = """
            {
              "id": 3842,
              "title": "string",
              "description": "string",
              "pageCount": "aa",
              "excerpt": "string",
              "publishDate": "1957-03-04T04:21:06.126Z"
            }
            """;

        Response resp = BookEndpoints.createRawBook(payload);
        resp.then().spec(badRequest400());

        String expected = "The JSON value could not be converted to System.Int32. Path: $.pageCount";
        assertFirstErrorContains(resp, "$.pageCount", expected);
    }

    @Test(description = "POST description as integer -> 400 + error on $.description")
    public void post_descriptionAsInteger_400_and_errorMessage() {
        String payload = """
            {
              "id": 3842,
              "title": "string",
              "description": 12345,
              "pageCount": 3849,
              "excerpt": "string",
              "publishDate": "1957-03-04T04:21:06.126Z"
            }
            """;

        Response resp = BookEndpoints.createRawBook(payload);
        resp.then().spec(badRequest400());

        String expected = "The JSON value could not be converted to System.String. Path: $.description";
        assertFirstErrorContains(resp, "$.description", expected);
    }
}
