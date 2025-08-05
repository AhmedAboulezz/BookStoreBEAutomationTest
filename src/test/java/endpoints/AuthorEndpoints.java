package endpoints;

import io.restassured.response.Response;
import static io.restassured.RestAssured.given;

public class AuthorEndpoints {

    private static final String BASE_PATH = "/api/v1/Authors";

    public static Response getAllAuthors() {
        return given().when().get(BASE_PATH);
    }

    public static Response getAuthorById(int id) {
        return given().when().get(BASE_PATH + "/" + id);
    }

    public static Response createAuthor(Object payload) {
        return given().body(payload).when().post(BASE_PATH);
    }

    public static Response updateAuthor(int id, Object payload) {
        return given().body(payload).when().put(BASE_PATH + "/" + id);
    }

    public static Response deleteAuthor(int id) {
        return given().when().delete(BASE_PATH + "/" + id);
    }

    public static Response getAuthorsByBookId(int bookId) {
        return given().when().get(BASE_PATH + "/authors/books/" + bookId);
    }
}