package kitchenpos.menu.acceptance;

import static org.assertj.core.api.Assertions.*;

import java.util.Arrays;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import kitchenpos.AcceptanceTest;
import kitchenpos.menu.dto.MenuGroupResponse;
import kitchenpos.menu.dto.MenuProductRequest;
import kitchenpos.menu.dto.MenuRequest;
import kitchenpos.menu.dto.ProductResponse;

public class MenuAcceptanceTest extends AcceptanceTest {
	@DisplayName("메뉴 생성 요청")
	@Test
	void create() {
		ProductResponse productResponse = ProductAcceptanceTest.상품_생성_요청("후라이드", 16000).as(ProductResponse.class);
		MenuGroupResponse menuGroupResponse = MenuGroupAcceptanceTest.메뉴_그룹_생성_요청("한마리메뉴").as(MenuGroupResponse.class);
		MenuRequest menuRequest = new MenuRequest("후라이드", 12000, menuGroupResponse.getId(),
			Arrays.asList(new MenuProductRequest(productResponse.getId(), 1)));

		ExtractableResponse<Response> response = 메뉴_생성_요청(menuRequest);

		메뉴_생성_성공(response);
	}

	@DisplayName("원래 가격보다 비싸면 메뉴 생성 실패")
	@Test
	void createWhenExpensive() {
		MenuRequest menuRequest = new MenuRequest("후라이드", 129000, 1L,
			Arrays.asList(new MenuProductRequest(1L, 1)));

		ExtractableResponse<Response> response = 메뉴_생성_요청(menuRequest);

		메뉴_생성_살패(response);
	}

	public static ExtractableResponse<Response> 메뉴_생성_요청(MenuRequest menuRequest) {
		return RestAssured.given().log().all()
			.contentType(MediaType.APPLICATION_JSON_VALUE)
			.body(menuRequest)
			.when().post("/api/menus")
			.then().log().all()
			.extract();
	}

	public static void 메뉴_생성_성공(ExtractableResponse<Response> response) {
		assertThat(response.statusCode()).isEqualTo(HttpStatus.CREATED.value());
	}

	public static void 메뉴_생성_살패(ExtractableResponse<Response> response) {
		assertThat(response.statusCode()).isEqualTo(HttpStatus.INTERNAL_SERVER_ERROR.value());
	}
}
