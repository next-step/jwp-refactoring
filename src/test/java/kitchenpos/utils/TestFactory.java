package kitchenpos.utils;

import static io.restassured.RestAssured.*;

import java.util.HashMap;
import java.util.Map;

import org.springframework.http.MediaType;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;

public class TestFactory {

    public static final String ID = "id";
    public static final String prefix = "api";

    public static ExtractableResponse<Response> get(String url, Long id) {
        return givenLog()
            .pathParam(ID, id)
            .get(prefix + url)
            .then().log().all()
            .extract();
    }

    public static ExtractableResponse<Response> get(String url) {
        return givenLog()
            .get(prefix + url)
            .then().log().all()
            .extract();
    }

    public static ExtractableResponse<Response> get(String url, Param param) {
        return givenLog()
            .queryParams(param.result())
            .get(prefix + url)
            .then().log().all()
            .extract();
    }

    public static ExtractableResponse<Response> post(String url, Object obj) {
        return givenLog()
            .when()
            .body(obj)
            .post(prefix + url)
            .then().log().all()
            .extract();
    }

    public static ExtractableResponse<Response> post(String url, Param param, Object obj) {
        return givenLog()
            .pathParams(param.result())
            .when()
            .body(obj)
            .post(prefix + url)
            .then().log().all()
            .extract();
    }

    public static ExtractableResponse<Response> update(String url, Long id, Object obj) {
        return givenLog()
            .pathParam(ID, id)
            .when()
            .body(obj)
            .patch(prefix + url)
            .then().log().all()
            .extract();
    }

    public static ExtractableResponse<Response> put(String url, String pathParam, Long id, Object obj) {
        return givenLog()
            .pathParam(pathParam, id)
            .when()
            .body(obj)
            .put(prefix + url)
            .then().log().all()
            .extract();
    }

    public static ExtractableResponse<Response> delete(String url,String pathParam, Long id) {
        return given()
            .pathParam(pathParam, id)
            .when()
            .delete(prefix + url)
            .then().log().all().extract();
    }

    public static <T> T toResponseData(ExtractableResponse<Response> resource, Class<T> clazz) {
        return resource.jsonPath().getObject(".", clazz);
    }

    private static RequestSpecification givenLog() {
        return given().log().all().contentType(MediaType.APPLICATION_JSON_VALUE);
    }
}
