package kitchenpos.product.acceptance;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.springframework.http.MediaType;

public final class RestAssuredHelper {

    private RestAssuredHelper(){
    }

    public static ExtractableResponse<Response> get(String uri, Object... params) {
        return RestAssured.given().log().all()
                .accept(MediaType.APPLICATION_JSON_VALUE)
                .when().get(uri, params)
                .then().log().all()
                .extract();
    }

    public static ExtractableResponse<Response> post(String uri, Object body) {
        return RestAssured.given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(body)
                .when().post(uri)
                .then().log().all()
                .extract();
    }

    public static ExtractableResponse<Response> putContainBody(String uri, Object body, Object... params) {
        return RestAssured.given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(body)
                .when().put(uri, params)
                .then().log().all()
                .extract();
    }

    public static ExtractableResponse<Response> put(String uri, Object... params) {
        return RestAssured.given().log().all()
                .when().put(uri, params)
                .then().log().all()
                .extract();
    }

    public static ExtractableResponse<Response> delete(String uri, Object... params) {
        return RestAssured.given().log().all()
                .when().delete(uri, params)
                .then().log().all()
                .extract();
    }
}
