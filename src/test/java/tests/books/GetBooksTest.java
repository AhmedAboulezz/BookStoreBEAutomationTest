package tests.books;

import base.TestBase;
import endpoints.BookEndpoints;
import io.restassured.common.mapper.TypeRef;
import io.restassured.response.Response;
import models.Book;
import org.testng.annotations.Test;
import specs.ResponseSpecs;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static org.testng.Assert.*;

/**
 * GET /api/v1/Books – basic happy path, field validations, data-specific validations.
 */
public class GetBooksTest extends TestBase {

    /** 1) Basic Happy Path: 200 + non-empty list */
    @Test(description = "GET /Books returns 200 and a non-empty array")
    public void getAllBooks_basicHappyPath() {
        Response resp = BookEndpoints.getAllBooks();
        resp.then().spec(ResponseSpecs.ok200());

        List<Book> books = resp.as(new TypeRef<List<Book>>() {});
        assertFalse(books.isEmpty(), "Books list should not be empty");
    }

    /** 2) Field Validations Per Book: required fields & types present */
    @Test(description = "Each returned book has required fields with valid types")
    public void getAllBooks_fieldValidations() {
        List<Book> books = BookEndpoints.getAllBooks()
                .then().spec(ResponseSpecs.ok200())
                .extract().as(new TypeRef<List<Book>>() {});

        assertFalse(books.isEmpty(), "Books list should not be empty");

        for (Book b : books) {
            // Required fields present
            assertNotNull(b.getId(), "id must be present");
            assertNotNull(b.getTitle(), "title must be present");
            assertNotNull(b.getDescription(), "description must be present");
            assertNotNull(b.getExcerpt(), "excerpt must be present");
            assertNotNull(b.getPageCount(), "pageCount must be present");
            assertNotNull(b.getPublishDate(), "publishDate must be present");

            // Basic type/logic checks
            assertTrue(b.getId() >= 0, "id should be >= 0");
            assertTrue(b.getPageCount() >= 0, "pageCount should be >= 0");
            // publishDate already deserialized to OffsetDateTime by TestBase config
        }
    }

    /** 3) Data-Specific Validations: unique IDs + non-negative pageCount for all */
    @Test(description = "IDs are unique and pageCount non-negative across all items")
    public void getAllBooks_dataSpecificValidations() {
        List<Book> books = BookEndpoints.getAllBooks()
                .then().spec(ResponseSpecs.ok200())
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
