package tests.books;

import base.TestBase;
import dataproviders.BookDataProviders;
import endpoints.BookEndpoints;
import io.restassured.response.Response;
import models.Book;

import org.testng.annotations.Test;
import utils.helpers;

import static org.testng.Assert.*;
import static specs.ResponseSpecs.*;

import java.time.OffsetDateTime;

public class PutBooksTest extends TestBase {

	@Test(description = "PUT /Books/9 happy path -> 200")
	public void put_happy200_id9() {

	    Book book = new Book()
	            .setId(9)
	            .setTitle("string")
	            .setDescription("string")
	            .setPageCount(3849)
	            .setExcerpt("string")
	            .setPublishDate(
	                OffsetDateTime.parse("1957-03-04T04:21:06.126Z"));

	    Response resp = BookEndpoints.updateBook(book);

	    resp.then().spec(ok200());

	    var jp = resp.jsonPath();
	    assertEquals(jp.getInt("id"),          book.getId());
	    assertEquals(jp.getString("title"),    book.getTitle());
	    assertEquals(jp.getString("description"), book.getDescription());
	    assertEquals(jp.getInt("pageCount"),   (int) book.getPageCount());
	    assertEquals(jp.getString("excerpt"),  book.getExcerpt());
	    assertEquals(jp.getString("publishDate"),
	                 book.getPublishDate().toString());
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
