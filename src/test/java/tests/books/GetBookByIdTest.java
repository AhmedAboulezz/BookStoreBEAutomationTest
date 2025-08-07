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

    @Test(
        dataProvider = "bookIdNumericCases",
        dataProviderClass = BookDataProviders.class,
        description = "Numeric id cases for GET /Books/{id}"
    )
    public void getBookById_numericCases(int id, int expectedStatus) {
        Response resp = BookEndpoints.getBookByIdJson(id);
        switch (expectedStatus) {
            case 200 -> resp.then().spec(ok200());
            case 400 -> resp.then().spec(badRequest400());
            case 404 -> resp.then().spec(notFound404());
            default  -> assertEquals(resp.getStatusCode(), expectedStatus, "Unexpected status");
        }
    }

    @Test(
        dataProvider = "bookIdStringCases",
        dataProviderClass = BookDataProviders.class,
        description = "Non-numeric id cases for GET /Books/{id}"
    )
    public void getBookById_stringCases(String idAsString, String errorKey, String expectedMessageContains) {
        Response resp = BookEndpoints.getBookByIdRaw(idAsString);
        resp.then().spec(badRequest400());
        helpers.assertFirstErrorContains(resp, errorKey, expectedMessageContains);
    }
}
