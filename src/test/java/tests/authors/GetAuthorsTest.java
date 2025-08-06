package tests.authors;

import base.TestBase;
import endpoints.AuthorEndpoints;
import io.restassured.common.mapper.TypeRef;
import io.restassured.response.Response;
import models.Author;
import org.testng.annotations.Test;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static org.testng.Assert.*;
import static specs.ResponseSpecs.*;
                                         
public class GetAuthorsTest extends TestBase {

    @Test(description = "GET /Authors returns 200 and a non-empty array")
    public void getAllAuthors_basicHappyPath() {
        Response resp = AuthorEndpoints.getAllAuthors();
        resp.then().spec(ok200());

        List<Author> authors = resp.as(new TypeRef<List<Author>>() {});
        assertFalse(authors.isEmpty(), "Authors list should not be empty");
    }

    @Test(description = "Content-Type contains API version v=1.0")
    public void getAllAuthors_contentTypeHasVersion() {
        Response resp = AuthorEndpoints.getAllAuthors();
        resp.then().spec(ok200());

        String contentType = resp.getHeader("Content-Type");
        assertNotNull(contentType, "Content-Type header should be present");
        assertTrue(contentType.contains("v=1.0"),
                "Expected Content-Type to contain API version v=1.0, but was: " + contentType);
    }

    @Test(description = "Each returned author has required fields with sensible values")
    public void getAllAuthors_fieldValidations() {
        List<Author> authors = AuthorEndpoints.getAllAuthors()
                .then().spec(ok200())
                .extract().as(new TypeRef<List<Author>>() {});

        assertFalse(authors.isEmpty(), "Authors list should not be empty");

        for (Author a : authors) {
            assertNotNull(a.getId(),        "id must be present");
            assertNotNull(a.getIdBook(),    "idBook must be present");
            assertNotNull(a.getFirstName(), "firstName must be present");
            assertNotNull(a.getLastName(),  "lastName must be present");

            assertTrue(a.getId() >= 0,        "id should be >= 0");
            assertTrue(a.getIdBook() >= 0,    "idBook should be >= 0");
            assertTrue(a.getFirstName().trim().length() > 0, "firstName should not be blank");
            assertTrue(a.getLastName().trim().length() > 0,  "lastName should not be blank");
        }
    }

    @Test(description = "IDs are unique across all authors")
    public void getAllAuthors_dataSpecificValidations() {
        List<Author> authors = AuthorEndpoints.getAllAuthors()
                .then().spec(ok200())
                .extract().as(new TypeRef<List<Author>>() {});

        assertFalse(authors.isEmpty(), "Authors list should not be empty");

        Set<Integer> ids = new HashSet<>();
        for (Author a : authors) {
            assertNotNull(a.getId(), "id must be present");
            assertTrue(ids.add(a.getId()), "Duplicate id found: " + a.getId());
        }
        assertEquals(ids.size(), authors.size(), "All ids should be unique");
    }
}
