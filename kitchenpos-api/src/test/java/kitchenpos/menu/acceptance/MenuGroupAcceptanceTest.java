package kitchenpos.menu.acceptance;

import static org.assertj.core.api.Assertions.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import kitchenpos.AcceptanceTest;
import kitchenpos.menu.dto.MenuGroupRequest;

public class MenuGroupAcceptanceTest extends AcceptanceTest {
	@DisplayName("메뉴 그룹 생성 요청")
	@Test
	void create() {
		ExtractableResponse<Response> response = 메뉴_그룹_생성_요청("추천메뉴");

		메뉴_그룹_생성_성공(response);
	}

	public static ExtractableResponse<Response> 메뉴_그룹_생성_요청(String name) {
		MenuGroupRequest menuGroupRequest = new MenuGroupRequest(name);

		return RestAssured.given().log().all()
			.contentType(MediaType.APPLICATION_JSON_VALUE)
			.body(menuGroupRequest)
			.when().post("/api/menu-groups")
			.then().log().all()
			.extract();
	}

	public static void 메뉴_그룹_생성_성공(ExtractableResponse<Response> response) {
		assertThat(response.statusCode()).isEqualTo(HttpStatus.CREATED.value());
	}
}
