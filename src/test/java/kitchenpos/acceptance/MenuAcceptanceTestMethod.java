package kitchenpos.acceptance;

import org.springframework.http.MediaType;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import kitchenpos.menu.dto.MenuRequest;

public class MenuAcceptanceTestMethod {
	public static ExtractableResponse<Response> createMenu(MenuRequest menu) {
		return RestAssured
			.given().log().all()
			.contentType(MediaType.APPLICATION_JSON_VALUE)
			.body(menu)
			.when().post("/api/menus")
			.then().log().all()
			.extract();
	}

	public static ExtractableResponse<Response> findAllMenu() {
		return RestAssured
			.given().log().all()
			.contentType(MediaType.APPLICATION_JSON_VALUE)
			.when().get("/api/menus")
			.then().log().all()
			.extract();
	}
}
