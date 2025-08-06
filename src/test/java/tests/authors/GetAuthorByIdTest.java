package tests.authors;

import base.TestBase;
import dataproviders.AuthorDataProviders;
import endpoints.AuthorEndpoints;
import io.restassured.response.Response;
import models.Author;
import org.testng.annotations.Test;
import utils.helpers;

import static org.testng.Assert.*;
import static specs.ResponseSpecs.*;

public class GetAuthorByIdTest extends TestBase {

    @Test(
        dataProvider = "authorHappyIds",
        dataProviderClass = AuthorDataProviders.class,
        description = "GET /Authors/{id} -> 200 and full author object with non-null fields"
    )
    public void getAuthorById_happy(int id) {
        Response resp = AuthorEndpoints.getAuthorByIdJson(id);
        resp.then().spec(ok200());

        Author a = resp.as(Author.class);
        assertNotNull(a, "Author response must not be null");
        assertEquals(a.getId(), Integer.valueOf(id), "Expected id to echo back");
        assertNotNull(a.getIdBook(), "idBook must not be null");
        assertNotNull(a.getFirstName(), "firstName must not be null");
        assertNotNull(a.getLastName(), "lastName must not be null");
    }

    @Test(
        dataProvider = "authorHappyIds",
        dataProviderClass = AuthorDataProviders.class,
        description = "GET /Authors/{id} -> Content-Type contains API version v=1.0"
    )
    public void getAuthorById_contentTypeHasVersion(int id) {
        Response resp = AuthorEndpoints.getAuthorByIdJson(id);
        resp.then().spec(ok200());

        String contentType = resp.getHeader("Content-Type");
        assertNotNull(contentType, "Content-Type header should be present");
        assertTrue(contentType.contains("v=1.0"),
                "Expected Content-Type to contain API version v=1.0, but was: " + contentType);
    }

    @Test(
        dataProvider = "authorBadStringIds",
        dataProviderClass = AuthorDataProviders.class,
        description = "GET /Authors/{bad} -> 400 with errors.id not-valid message"
    )
    public void getAuthorById_nonNumeric_returns400(String badId, String expectedError) {
        Response resp = AuthorEndpoints.getAuthorByIdRaw(badId);
        resp.then().spec(badRequest400());
        helpers.assertFirstErrorContains(resp, "id", expectedError);
    }

    @Test(
        dataProvider = "authorNumeric404Ids",
        dataProviderClass = AuthorDataProviders.class,
        description = "GET /Authors/{id} with non-existent/invalid numeric -> 404 Not Found"
    )
    public void getAuthorById_returns404(int id404) {
        Response resp = AuthorEndpoints.getAuthorByIdJson(id404);
        resp.then().spec(notFound404());
    }
}
