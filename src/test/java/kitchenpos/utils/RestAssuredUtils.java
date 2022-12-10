package kitchenpos.utils;

import org.springframework.http.MediaType;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;

public class RestAssuredUtils {

	public static <T> ExtractableResponse<Response> post(String path, T requestBody) {
		return RestAssured.given().log().all()
			.contentType(MediaType.APPLICATION_JSON_VALUE)
			.body(requestBody)
			.when().log().all()
			.post(path)
			.then().log().all()
			.extract();
	}

	public static ExtractableResponse<Response> get(String path) {
		return RestAssured.given().log().all()
			.accept(MediaType.APPLICATION_JSON_VALUE)
			.when().log().all()
			.get(path)
			.then().log().all()
			.extract();
	}
}
