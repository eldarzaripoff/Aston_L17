package com.postman_echo;

import com.fasterxml.jackson.core.JsonProcessingException;
import dto.deleteRequest.DeleteRequestDTO;
import dto.getRequest.GetRequestDTO;
import dto.patchRequest.PatchRequestDTO;
import dto.postRequest.PostRequestDTO;
import dto.postRequest.PostRequestFormDTO;
import dto.putRequest.PutRequestDTO;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.HashMap;
import java.util.Map;
import java.util.stream.Stream;

import static io.restassured.RestAssured.given;
import static org.junit.jupiter.api.Assertions.*;

public class Tests {
    @DisplayName("Тест GET-запроса")
    @ParameterizedTest
    @MethodSource("expectedDataProvider")
    public void testGetRequest(Map<String, String> inputData) {
        Response response = given()
                .contentType(ContentType.JSON)
                .queryParams(inputData)
                .when()
                .get("https://postman-echo.com/get")
                .then()
                .log().all()
                .statusCode(200)
                .extract().response();

        GetRequestDTO getRequestDTO = response.as(GetRequestDTO.class);
        Assertions.assertAll(
                () -> assertEquals(inputData.get("foo1"), getRequestDTO.getArgs().getFoo1(),
                        "Значение для ключа 'foo1' отличается от ожидаемого"),
                () -> assertEquals(inputData.get("foo2"), getRequestDTO.getArgs().getFoo2(),
                        "Значение для ключа 'foo2' отличается от ожидаемого")
        );
    }

    @DisplayName("Тест POST-запроса, Body: 'raw'")
    @Test
    public void testPostRequest() throws JsonProcessingException {
        String requestBody = "{\n    \"test\": \"value\"\n}";

        Response response = given()
                .contentType(ContentType.JSON)
                .body(requestBody)
                .when()
                .post("https://postman-echo.com/post")
                .then()
                .log().all()
                .statusCode(200)
                .extract().response();

        PostRequestDTO postRequestDTO = response.as(PostRequestDTO.class);
        Assertions.assertAll(
                () -> assertNull(postRequestDTO.getArgs().get("args")),
                () -> assertEquals("value", postRequestDTO.getData().get("test")),
                () -> assertNull(postRequestDTO.getFiles().get("files")),
                () -> assertNull(postRequestDTO.getForm().get("form"))
        );
    }

    @DisplayName("Тест POST-запроса, Body: 'x-www-form-urlencoded'")
    @ParameterizedTest
    @MethodSource("expectedDataProvider")
    public void testPostFormRequest(Map<String, String> inputData) {
        Response response = given()
                .contentType("application/x-www-form-urlencoded; charset=utf-8")
                .formParams(inputData)
                .when()
                .post("https://postman-echo.com/post")
                .then()
                .log().all()
                .statusCode(200)
                .extract().response();

        PutRequestDTO postRequestDTO = response.as(PutRequestDTO.class);
        Assertions.assertAll(
                () -> assertNull(postRequestDTO.getArgs().get("args")),
                () -> assertTrue(postRequestDTO.getData().isEmpty()),
                () -> assertNull(postRequestDTO.getFiles().get("files")),
                () -> assertEquals("bar1", postRequestDTO.getForm().get("foo1")),
                () -> assertEquals("bar2", postRequestDTO.getForm().get("foo2")),
                () -> assertEquals(inputData, postRequestDTO.getJson())
        );
    }

    @DisplayName("Тест PUT-запроса")
    @ParameterizedTest
    @MethodSource("expectedDataProvider")
    public void testPutRequest(Map<String, String> inputData) {
        Response response = given()
                .contentType(ContentType.JSON)
                .formParams(inputData)
                .when()
                .put("https://postman-echo.com/put")
                .then()
                .log().all()
                .statusCode(200)
                .extract().response();

        PostRequestFormDTO requestDTO = response.as(PostRequestFormDTO.class);
        Assertions.assertAll(
                () -> assertNull(requestDTO.getArgs().get("args")),
                () -> assertEquals("foo1=bar1&foo2=bar2", requestDTO.getData()),
                () -> assertNull(requestDTO.getFiles().get("files")),
                () -> assertNull(requestDTO.getForm().get("form")),
                () -> assertEquals("application/json", requestDTO.getHeaders().get("content-type")),
                () -> assertNull(requestDTO.getJson())
        );

    }

    @DisplayName("Тест PATCH-запроса")
    @Test
    public void testPatchRequest() {
        Response response = given()
                .contentType(ContentType.JSON)
                .when()
                .patch("https://postman-echo.com/patch?foo1=newBar1&foo2=bar2")
                .then()
                .log().all()
                .statusCode(200)
                .extract().response();

        PatchRequestDTO requestDTO = response.as(PatchRequestDTO.class);
        Assertions.assertAll(
                () -> assertEquals("newBar1", requestDTO.getArgs().getFoo1()),
                () -> assertEquals("bar2", requestDTO.getArgs().getFoo2()),
                () -> assertNull(requestDTO.getData().get("data")),
                () -> assertNull(requestDTO.getFiles().get("files")),
                () -> assertNull(requestDTO.getForm().get("form")),
                () -> assertEquals("application/json", requestDTO.getHeaders().get("content-type")),
                () -> assertNull(requestDTO.getJson())
        );
    }

    @DisplayName("Тест DELETE-запроса")
    @Test
    public void testDeleteRequest() {
        Response response = given()
                .contentType(ContentType.JSON)
                .when()
                .delete("https://postman-echo.com/delete?foo1=newBar1&foo2=bar2")
                .then()
                .log().all()
                .statusCode(200)
                .extract().response();

        DeleteRequestDTO requestDTO = response.as(DeleteRequestDTO.class);
        Assertions.assertAll(
                () -> assertEquals("newBar1", requestDTO.getArgs().getFoo1()),
                () -> assertEquals("bar2", requestDTO.getArgs().getFoo2()),
                () -> assertNull(requestDTO.getData().get("data")),
                () -> assertNull(requestDTO.getFiles().get("files")),
                () -> assertNull(requestDTO.getForm().get("form")),
                () -> assertEquals("application/json", requestDTO.getHeaders().get("content-type")),
                () -> assertNull(requestDTO.getJson())
        );
    }

    private static Stream<Map<String, String>> expectedDataProvider() {
        Map<String, String> expectedData = new HashMap<>();
        expectedData.put("foo1", "bar1");
        expectedData.put("foo2", "bar2");
        return Stream.of(expectedData);
    }
}
