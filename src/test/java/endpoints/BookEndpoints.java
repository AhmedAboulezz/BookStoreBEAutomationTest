package endpoints;

import io.restassured.response.Response;
import specs.RequestSpecs;

import static io.restassured.RestAssured.given;

public class BookEndpoints {

    private static final String BASE_PATH = "https://fakerestapi.azurewebsites.net/api/v1/Books";

    /** GET all books – mirrors the cURL: Accept: text/plain; v=1.0 */
    public static Response getAllBooks() {
        return given()
                .spec(RequestSpecs.acceptPlainV10())
                .when()
                .get(BASE_PATH);
    }
    
    
    /** GET /Books/{id} with Accept: application/json; v=1.0 (handy for parsing). */
    public static Response getBookByIdJson(int id) {
        return given()
                .spec(RequestSpecs.jsonV10())
            .when()
                .get(BASE_PATH+"/{id}", id);
    }

    /** GET /Books/{idSegment} where idSegment can be non-numeric (e.g., "a"). */
    public static Response getBookByIdRaw(String idSegment) {
        return given()
                .spec(RequestSpecs.jsonV10())
            .when()
                .get(BASE_PATH+"/{id}", idSegment);
    }


    public static Response createBook(Object payload) {
        return given()
                .spec(RequestSpecs.jsonV10())
                .body(payload)
                .when()
                .post(BASE_PATH);
    }
    public static Response updateBook(int id, Object payload) {
        return given().header("Content-Type", "application/json; charset=utf-8; v=1.0").header("Accept", "*/*").body(payload).when().put(BASE_PATH + "/" + id);
    }
    
    public static Response createRawBook(String jsonPayload) {
        return given()
                .spec(RequestSpecs.jsonV10())
                .body(jsonPayload)
                .when()
                .post(BASE_PATH);
    }

 

    /** PUT using raw JSON (for negative/format tests) */
    public static Response updateBookRaw(int id, String jsonPayload) {
        return given()
                .spec(RequestSpecs.jsonV10())
                .body(jsonPayload)
            .when()
                .put(BASE_PATH + "/{id}", id);
    }
    
    
    /** DELETE /Books/{id} where id is an integer (uses Accept JSON to read error bodies). */
    public static Response deleteBook(int id) {
        return given()
                .spec(RequestSpecs.acceptJsonV10())
                .when()
                .delete(BASE_PATH + "/{id}", id);
    }

    /** DELETE /Books/{id} with a raw (non-integer) segment, e.g. "abc" or a giant number string. */
    public static Response deleteBookRawPath(String idSegment) {
        return given()
                .spec(RequestSpecs.acceptJsonV10())
                .when()
                .delete(BASE_PATH + "/" + idSegment);
    }

    /** DELETE /Books (no id) */
    public static Response deleteBooksCollection() {
        return given()
                .spec(RequestSpecs.base())
                .when()
                .delete(BASE_PATH);
    }


}
