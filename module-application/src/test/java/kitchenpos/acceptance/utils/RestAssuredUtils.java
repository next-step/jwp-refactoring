package kitchenpos.acceptance.utils;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.springframework.http.MediaType;

public class RestAssuredUtils {

	public static <T> ExtractableResponse<Response> post(String path, T requestBody) {
		return RestAssured.given()
						  .log()
						  .all()
						  .contentType(MediaType.APPLICATION_JSON_VALUE)
						  .body(requestBody)
						  .when()
						  .log()
						  .all()
						  .post(path)
						  .then()
						  .log()
						  .all()
						  .extract();
	}

	public static ExtractableResponse<Response> get(String path) {
		return RestAssured.given()
						  .log()
						  .all()
						  .accept(MediaType.APPLICATION_JSON_VALUE)
						  .when()
						  .log()
						  .all()
						  .get(path)
						  .then()
						  .log()
						  .all()
						  .extract();
	}

	public static <T> ExtractableResponse<Response> put(String path, Long id, T requestBody) {
		return RestAssured.given()
						  .log()
						  .all()
						  .contentType(MediaType.APPLICATION_JSON_VALUE)
						  .body(requestBody)
						  .when()
						  .log()
						  .all()
						  .put(path, id)
						  .then()
						  .log()
						  .all()
						  .extract();
	}

	public static ExtractableResponse<Response> delete(String requestPath, Long id) {
		return RestAssured.given()
						  .log()
						  .all()
						  .when()
						  .log()
						  .all()
						  .delete(requestPath, id)
						  .then()
						  .log()
						  .all()
						  .extract();
	}
}
