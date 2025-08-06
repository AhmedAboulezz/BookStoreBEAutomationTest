package tests.books;

import base.TestBase;
import dataproviders.BookDataProviders;
import endpoints.BookEndpoints;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import org.testng.annotations.Test;
import utils.helpers;

import static org.testng.Assert.*;
import static specs.ResponseSpecs.*;

public class PostBooksTest extends TestBase {

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

    @Test(
        dataProvider = "postInvalidPayloads",
        dataProviderClass = BookDataProviders.class,
        description = "POST invalid payloads -> 400 + field-specific error"
    )
    public void post_invalidPayloads_400_withFieldError(String payload, String errorPath, String expectedSubstring) {
        Response resp = BookEndpoints.createRawBook(payload);
        resp.then().spec(badRequest400());
        helpers.assertFirstErrorContains(resp, errorPath, expectedSubstring);
    }
}
