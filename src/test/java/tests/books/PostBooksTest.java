package tests.books;

import endpoints.BookEndpoints;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import org.testng.annotations.Test;

import base.TestBase;

import static org.testng.Assert.*;

/**
 * POST /api/v1/Books strict scenarios requested:
 * 1) Body without id -> 400
 * 2) Bad publishDate -> 400 + assert errors.$.publishDate
 * 3) Happy scenario (no id, valid fields) -> 200 and same fields echoed back
 * 4) excerpt as integer -> 400 + assert errors.$.excerpt
 * 5) pageCount as string -> 400 + assert errors.$.pageCount
 * 6) description as integer -> 400 + assert errors.$.description
 */
public class PostBooksTest extends TestBase {

	private void assertFirstErrorContains(Response resp, String jsonPointer, String expectedSubstring) {
	    String path = "errors.'" + jsonPointer + "'[0]";
	    String actual = resp.jsonPath().getString(path);
	    assertNotNull(actual, "Expected error message for key: " + jsonPointer);
	    assertTrue(actual.contains(expectedSubstring),
	        "Expected error for '" + jsonPointer + "' to contain:\n" + expectedSubstring + "\nbut got:\n" + actual);
	}


    /** 1) Sending body without id should return 400 */
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
        assertEquals(resp.getStatusCode(), 400, "Expected 400 when 'id' is omitted");
    }

    /**
     * 2) without proper publishDate -> 400 with specific error
     * "errors": {
     *   "$.publishDate": [
     *     "The JSON value could not be converted to System.DateTime. Path: $.publishDate | LineNumber: 6 | BytePositionInLine: 21."
     *   ]
     * }
     */
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
        assertEquals(resp.getStatusCode(), 400, "Expected 400 for invalid publishDate");

        String expected = "The JSON value could not be converted to System.DateTime. Path: $.publishDate";
        assertFirstErrorContains(resp, "$.publishDate", expected);
    }

    /**
     * 3) Happy scenario (no id) -> 200 with same fields echoed back (no id in response)
     */
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
        assertEquals(resp.getStatusCode(), 200, "Expected 200 on happy path");

        JsonPath jp = resp.jsonPath();
        assertEquals(jp.getString("title"), title);
        assertEquals(jp.getString("description"), desc);
        assertEquals((Integer) jp.getInt("pageCount"), Integer.valueOf(pageCount));
        assertEquals(jp.getString("excerpt"), excerpt);
        assertEquals(jp.getString("publishDate"), publishDate);
    }

    /**
     * 4) Send Excerpt as integer -> 400 + error on $.excerpt
     * "errors": {
     *   "$.excerpt": [
     *     "The JSON value could not be converted to System.String. Path: $.excerpt | LineNumber: 4 | BytePositionInLine: 14."
     *   ]
     * }
     */
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
        assertEquals(resp.getStatusCode(), 400, "Expected 400 for non-string excerpt");

        String expected = "The JSON value could not be converted to System.String. Path: $.excerpt";
        assertFirstErrorContains(resp, "$.excerpt", expected);
    }

    /**
     * 5) Send pageCount as string -> 400 + error on $.pageCount
     * "errors": {
     *   "$.pageCount": [
     *     "The JSON value could not be converted to System.Int32. Path: $.pageCount | LineNumber: 3 | BytePositionInLine: 19."
     *   ]
     * }
     */
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
        assertEquals(resp.getStatusCode(), 400, "Expected 400 for non-integer pageCount");

        String expected = "The JSON value could not be converted to System.Int32. Path: $.pageCount";
        assertFirstErrorContains(resp, "$.pageCount", expected);
    }

    /**
     * 6) description as integer -> 400 + error on $.description
     * "errors": {
     *   "$.description": [
     *     "The JSON value could not be converted to System.String. Path: $.description | LineNumber: 2 | BytePositionInLine: 18."
     *   ]
     * }
     */
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
        assertEquals(resp.getStatusCode(), 400, "Expected 400 for non-string description");

        String expected = "The JSON value could not be converted to System.String. Path: $.description";
        assertFirstErrorContains(resp, "$.description", expected);
    }
}
