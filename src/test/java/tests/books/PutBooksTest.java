package tests.books;

import base.TestBase;
import dataproviders.BookDataProviders;
import endpoints.BookEndpoints;
import io.restassured.response.Response;
import org.testng.annotations.Test;
import utils.helpers;

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

    @Test(
        dataProvider = "putInvalidPayloads",
        dataProviderClass = BookDataProviders.class,
        description = "PUT invalid payloads -> 400 + field-specific error"
    )
    public void put_invalidPayloads_return400_withErrorDetails(
            String payload, String errorPath, String expectedSubstring) {

        Response resp = BookEndpoints.updateBookRaw(9, payload);
        resp.then().spec(badRequest400());
        helpers.assertFirstErrorContains(resp, errorPath, expectedSubstring);
    }
}
