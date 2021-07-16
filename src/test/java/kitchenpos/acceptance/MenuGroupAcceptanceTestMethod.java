package kitchenpos.acceptance;

import org.springframework.http.MediaType;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import kitchenpos.domain.MenuGroup;

public class MenuGroupAcceptanceTestMethod {
	public static ExtractableResponse<Response> createMenuGroup(MenuGroup menuGroup) {
		return RestAssured
			.given().log().all()
			.contentType(MediaType.APPLICATION_JSON_VALUE)
			.body(menuGroup)
			.when().post("/api/menu-groups")
			.then().log().all()
			.extract();
	}

	public static ExtractableResponse<Response> findMenuGroup() {
		return RestAssured
			.given().log().all()
			.contentType(MediaType.APPLICATION_JSON_VALUE)
			.when().get("/api/menu-groups")
			.then().log().all()
			.extract();
	}
}
