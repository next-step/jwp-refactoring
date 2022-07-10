package kitchenpos.product.acceptance;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.springframework.http.MediaType;

import java.util.Map;

public class RestAssuredRequest {

    private RestAssuredRequest() {}

    public static ExtractableResponse<Response> getRequest(
            String path, Map<String, Object> params, Object... pathVariables
    ) {
        return RestAssured.given().log().all()
                .params(params)
                .when().get(path, pathVariables)
                .then().log().all()
                .extract();
    }

    public static ExtractableResponse<Response> postRequest(
            String path, Map<String, Object> params, Object body, Object... pathVariables
    ) {
        return RestAssured.given().log().all()
                .params(params)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(body)
                .when().post(path, pathVariables)
                .then().log().all()
                .extract();
    }

    public static ExtractableResponse<Response> putRequest(
            String path, Map<String, Object> params, Object body, Object... pathVariables
    ) {
        return RestAssured.given().log().all()
                .params(params)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(body)
                .when().put(path, pathVariables)
                .then().log().all()
                .extract();
    }

    public static ExtractableResponse<Response> deleteRequest(
            String path, Map<String, Object> params, Object... pathVariables
    ) {
        return RestAssured.given().log().all()
                .params(params)
                .when().delete(path, pathVariables)
                .then().log().all()
                .extract();
    }
}
