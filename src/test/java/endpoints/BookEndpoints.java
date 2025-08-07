package endpoints;

import io.restassured.response.Response;
import models.Book;
import specs.RequestSpecs;

import static io.restassured.RestAssured.given;

import config.ConfigurationManager;

public class BookEndpoints {

    private static final String BASE_PATH = ConfigurationManager.getBaseUrl()+"Books";

    public static Response getAllBooks() {
        return given()
                .spec(RequestSpecs.jsonV10())
                .when()
                .get(BASE_PATH);
    }
    
    
    public static Response getBookByIdJson(int id) {
        return given()
                .spec(RequestSpecs.jsonV10())
            .when()
                .get(BASE_PATH+"/{id}", id);
    }

    public static Response getBookByIdRaw(String idSegment) {
        return given()
                .spec(RequestSpecs.jsonV10())
            .when()
                .get(BASE_PATH+"/{id}", idSegment);
    }



    public static Response createRawBook(String jsonPayload) {
        return given()
                .spec(RequestSpecs.jsonV10())
                .body(jsonPayload)
                .when()
                .post(BASE_PATH);
    }

 
    public static Response updateBookRaw(int id, String jsonPayload) {
        return given()
                .spec(RequestSpecs.jsonV10())
                .body(jsonPayload)
            .when()
                .put(BASE_PATH + "/{id}", id);
    }
    
    
    public static Response deleteBook(int id) {
        return given()
                .spec(RequestSpecs.jsonV10())
                .when()
                .delete(BASE_PATH + "/{id}", id);
    }


    public static Response deleteBookRawPath(String idSegment) {
        return given()
                .spec(RequestSpecs.jsonV10())
                .when()
                .delete(BASE_PATH + "/" + idSegment);
    }


    public static Response deleteBooksCollection() {
        return given()
                .spec(RequestSpecs.jsonV10())
                .when()
                .delete(BASE_PATH);
    }
    

    public static Response createBook(Book book) {
        return given()
                .spec(RequestSpecs.jsonV10())
                .body(book)              
            .when()
                .post(BASE_PATH);
    }

    public static Response updateBook(Book book) {
        return given()
                .spec(RequestSpecs.jsonV10())
                .body(book)
            .when()
                .put(BASE_PATH + "/{id}", book.getId());
    }


}
