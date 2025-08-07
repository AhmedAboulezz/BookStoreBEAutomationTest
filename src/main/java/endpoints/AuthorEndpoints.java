package endpoints;

import io.restassured.response.Response;

import static io.restassured.RestAssured.given;
import static specs.RequestSpecs.jsonV10;

import config.ConfigurationManager;

public final class AuthorEndpoints {
    private AuthorEndpoints() {}

    private static final String AUTHORS = ConfigurationManager.getBaseUrl()+"/Authors";

    public static Response getAllAuthors() {
        return given()
                .spec(jsonV10())
        .when()
                .get(AUTHORS)
        .then()
                .extract().response();
    }
    
    public static Response deleteAuthor(int id) {
        return given()
                .spec(jsonV10())
                .pathParam("id", id)
        .when()
                .delete(AUTHORS + "/{id}")
        .then()
                .extract().response();
    }

    public static Response deleteAuthorRawPath(String idLiteral) {
        return given()
                .spec(jsonV10())
        .when()
                .delete(AUTHORS + "/" + idLiteral)
        .then()
                .extract().response();
    }

    public static Response deleteAuthorsCollection() {
        return given()
                .spec(jsonV10())
        .when()
                .delete(AUTHORS)
        .then()
                .extract().response();
    }
    
    public static Response getAuthorByIdJson(int id) {
        return given()
                .spec(jsonV10())
                .pathParam("id", id)
        .when()
                .get(AUTHORS + "/{id}")
        .then()
                .extract().response();
    }

    public static Response getAuthorByIdRaw(String idLiteral) {
        return given()
                .spec(jsonV10())
        .when()
                .get(AUTHORS + "/" + idLiteral)
        .then()
                .extract().response();
    }
}
