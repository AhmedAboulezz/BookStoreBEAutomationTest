package tests.books;

import base.TestBase;
import dataproviders.BookDataProviders;
import endpoints.BookEndpoints;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import models.Book;                        
import org.testng.annotations.Test;
import utils.helpers;

import java.time.OffsetDateTime;            

import static org.testng.Assert.*;
import static specs.ResponseSpecs.*;

public class PostBooksTest extends TestBase {

    @Test(description = "POST without id -> 400")
    public void post_withoutId_400() {

        Book book = new Book()
                .setTitle("string")
                .setDescription("string")
                .setPageCount(3849)
                .setExcerpt("string")
                .setPublishDate(
                    OffsetDateTime.parse("1957-03-04T04:21:06.126Z"));

        Response resp = BookEndpoints.createBook(book);
        resp.then().spec(badRequest400());
    }

    @Test(description = "POST happy path (no id) -> 200 and same fields echoed")
    public void post_happy_noId_200_echoedFields() {

        Book book = new Book()
                .setTitle("string")
                .setDescription("string")
                .setPageCount(3849)
                .setExcerpt("string")
                .setPublishDate(
                    OffsetDateTime.parse("1957-03-04T04:21:06.126Z"));

        Response resp = BookEndpoints.createBook(book);
        resp.then().spec(ok200());

        JsonPath jp = resp.jsonPath();
        assertEquals(jp.getString("title"),       book.getTitle());
        assertEquals(jp.getString("description"), book.getDescription());
        assertEquals((Integer) jp.getInt("pageCount"),
                     book.getPageCount());
        assertEquals(jp.getString("excerpt"),     book.getExcerpt());
        assertEquals(jp.getString("publishDate"),
                     book.getPublishDate().toString());
    }


    @Test(
        dataProvider = "postInvalidPayloads",
        dataProviderClass = BookDataProviders.class,
        description = "POST invalid payloads -> 400 + field-specific error"
    )
    public void post_invalidPayloads_400_withFieldError(
            String payload, String errorPath, String expectedSubstring) {

        Response resp = BookEndpoints.createRawBook(payload);
        resp.then().spec(badRequest400());
        helpers.assertFirstErrorContains(resp, errorPath, expectedSubstring);
    }
}
