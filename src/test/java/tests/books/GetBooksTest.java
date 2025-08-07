package tests.books;

import base.TestBase;
import endpoints.BookEndpoints;
import io.restassured.common.mapper.TypeRef;
import io.restassured.response.Response;
import models.Book;
import org.testng.annotations.Test;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static org.testng.Assert.*;
import static specs.ResponseSpecs.*;  


public class GetBooksTest extends TestBase {

    @Test(description = "GET /Books returns 200 and a non-empty array")
    public void getAllBooks_basicHappyPath() {
        Response resp = BookEndpoints.getAllBooks();
        resp.then().spec(ok200());

        List<Book> books = resp.as(new TypeRef<List<Book>>() {});
        assertFalse(books.isEmpty(), "Books list should not be empty");
    }

    @Test(description = "Content-Type contains API version v=1.0")
    public void getAllBooks_contentTypeHasVersion() {
        Response resp = BookEndpoints.getAllBooks();
        resp.then().spec(ok200());

        String contentType = resp.getHeader("Content-Type");
        assertNotNull(contentType, "Content-Type header should be present");
        assertTrue(contentType.contains("v=1.0"),
                "Expected Content-Type to contain API version v=1.0, but was: " + contentType);
    }

    @Test(description = "Each returned book has required fields with valid types")
    public void getAllBooks_fieldValidations() {
        List<Book> books = BookEndpoints.getAllBooks()
                .then().spec(ok200())
                .extract().as(new TypeRef<List<Book>>() {});

        assertFalse(books.isEmpty(), "Books list should not be empty");

        for (Book b : books) {
            assertNotNull(b.getId(),          "id must be present");
            assertNotNull(b.getPageCount(),   "pageCount must be present");
            assertNotNull(b.getPublishDate(), "publishDate must be present");

            assertTrue(b.getId()        >= 0, "id should be ≥ 0");
            assertTrue(b.getPageCount() >= 0, "pageCount should be ≥ 0");

            if (b.getTitle() != null) {
                assertFalse(b.getTitle().trim().isEmpty(),
                            "title should not be blank when present");
            }
            if (b.getDescription() != null) {
                assertFalse(b.getDescription().trim().isEmpty(),
                            "description should not be blank when present");
            }
            if (b.getExcerpt() != null) {
                assertFalse(b.getExcerpt().trim().isEmpty(),
                            "excerpt should not be blank when present");
            }
        }
    }


    @Test(description = "IDs are unique and pageCount non-negative across all items")
    public void getAllBooks_dataSpecificValidations() {
        List<Book> books = BookEndpoints.getAllBooks()
                .then().spec(ok200())
                .extract().as(new TypeRef<List<Book>>() {});

        assertFalse(books.isEmpty(), "Books list should not be empty");

        Set<Integer> ids = new HashSet<>();
        for (Book b : books) {
            assertNotNull(b.getId(), "id must be present");
            assertTrue(ids.add(b.getId()), "Duplicate id found: " + b.getId());
            assertNotNull(b.getPageCount(), "pageCount must be present");
            assertTrue(b.getPageCount() >= 0, "pageCount should be >= 0 for id=" + b.getId());
        }
        assertEquals(ids.size(), books.size(), "All ids should be unique");
    }
}
