package kitchenpos.menu.acceptance;

import static org.assertj.core.api.Assertions.*;

import java.math.BigDecimal;
import java.util.List;

import org.assertj.core.util.Lists;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import kitchenpos.AcceptanceTest;
import kitchenpos.menu.dto.MenuProductRequest;
import kitchenpos.menu.dto.MenuRequest;
import kitchenpos.menu.dto.MenuResponse;

@DisplayName("메뉴 기능 인수테스트")
class MenuAcceptanceTest extends AcceptanceTest {
	private static MenuProductRequest 후라이드_2마리 = new MenuProductRequest(1, 2);
	private static MenuProductRequest 양념_1마리 = new MenuProductRequest(2, 1);
	private static MenuRequest 후라이드2_양념1 = new MenuRequest("후라이드+후라이드+양념", new BigDecimal(40000), 1L,
		Lists.newArrayList(후라이드_2마리, 양념_1마리));

	@Test
	@DisplayName("메뉴 생성 테스트")
	public void crateMenuTest() {
		//given
		//when
		ExtractableResponse<Response> response = 메뉴_생성_요청(후라이드2_양념1);

		//then
		메뉴_생성_성공(response);
	}

	@Test
	@DisplayName("메뉴그룹이 없어서 메뉴생성 실패")
	public void crateMenuFailNoneMenuGroupTest() {
		//given
		MenuRequest 메뉴그룹없는_메뉴 = new MenuRequest("후라이드+후라이드+양념", new BigDecimal(40000), 99L,
			Lists.newArrayList(후라이드_2마리, 양념_1마리));

		//when
		ExtractableResponse<Response> response = 메뉴_생성_요청(메뉴그룹없는_메뉴);

		//then
		메뉴_생성_실패(response, "메뉴그룹이 존재하지 않습니다.");
	}


	@Test
	@DisplayName("상품이 없어서 메뉴생성 실패")
	public void crateMenuFailNoneProductTest() {
		//given
		MenuRequest 상품없는_메뉴 = new MenuRequest("후라이드+후라이드+양념", new BigDecimal(40000), 1L,
			Lists.newArrayList(후라이드_2마리, new MenuProductRequest(99, 1)));

		//when
		ExtractableResponse<Response> response = 메뉴_생성_요청(상품없는_메뉴);

		//then
		메뉴_생성_실패(response, "상품이 존재하지 않습니다");
	}

	@Test
	@DisplayName("메뉴가격이 0보다 작아서 메뉴생성 실패")
	public void crateMenuFailPriceLessThanZeroTest() {
		//given
		MenuRequest 가격이_0보다_작은_메뉴 = new MenuRequest("후라이드+후라이드+양념", new BigDecimal(-1), 1L,
			Lists.newArrayList(후라이드_2마리, 양념_1마리));

		//when
		ExtractableResponse<Response> response = 메뉴_생성_요청(가격이_0보다_작은_메뉴);

		//then
		메뉴_생성_실패(response, "가격은 0보다 작을 수 없습니다");
	}

	@Test
	@DisplayName("메뉴가격이 상품들 가격합보다 커서 메뉴생성 실패")
	public void crateMenuFailPriceLessThanProductsPriceSumTest() {
		//given
		MenuRequest 가격이_상품들_가격합보다_큰_메뉴 = new MenuRequest("후라이드+후라이드+양념", new BigDecimal(60000), 1L,
			Lists.newArrayList(후라이드_2마리, 양념_1마리));

		//when
		ExtractableResponse<Response> response = 메뉴_생성_요청(가격이_상품들_가격합보다_큰_메뉴);

		//then
		메뉴_생성_실패(response, "메뉴의 가격은 상품들의 가격합보다 작거나 같아야 합니다");
	}

	@Test
	@DisplayName("메뉴 목록 조회 테스트")
	public void findAllMenuTest() {
		//given
		//when
		ExtractableResponse<Response> response = 메뉴_목록_조회_요청();

		//then
		메뉴_목록_조회_성공(response);
	}


	private void 메뉴_생성_실패(ExtractableResponse<Response> response, String message) {
		assertThat(response.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value());
		assertThat(response.body().asString()).isEqualTo(message);
	}

	private void 메뉴_목록_조회_성공(ExtractableResponse<Response> response) {
		List<MenuResponse> menuResponses = response.jsonPath().getList(".", MenuResponse.class);
		assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
		assertThat(menuResponses).hasSizeGreaterThanOrEqualTo(6);
	}

	private ExtractableResponse<Response> 메뉴_목록_조회_요청() {
		return RestAssured.given().log().all()
			.accept(MediaType.APPLICATION_JSON_VALUE)
			.when().get("/api/menus/")
			.then().log().all()
			.extract();
	}

	private void 메뉴_생성_성공(ExtractableResponse<Response> response) {
		assertThat(response.statusCode()).isEqualTo(HttpStatus.CREATED.value());
		assertThat(response.header("Location")).isEqualTo("/api/menus/7");
	}

	private ExtractableResponse<Response> 메뉴_생성_요청(MenuRequest menuRequest) {
		return RestAssured.given().log().all()
			.contentType(MediaType.APPLICATION_JSON_VALUE)
			.body(menuRequest)
			.when().post("/api/menus/")
			.then().log().all()
			.extract();
	}

}
