package kitchenpos.menu.domain;

import static org.assertj.core.api.Assertions.*;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import kitchenpos.menu.dto.MenuProductRequest;
import kitchenpos.menu.dto.MenuRequest;
import kitchenpos.menu.dto.MenuResponse;
import kitchenpos.product.dto.ProductResponse;

public class MenuAcceptanceStaticTest {

	public static final String MENU_PATH = "/api/menus";

	public static ExtractableResponse<Response> 메뉴_목록_조회_요청() {
		return RestAssured.given().log().all()
			.when()
			.get(MENU_PATH)
			.then().log().all()
			.extract();
	}

	public static void 메뉴_목록_조회됨(ExtractableResponse<Response> response, List<Long> idList) {
		List<Long> responseIdList = response.body()
			.jsonPath().getList(".", MenuResponse.class)
			.stream()
			.map(MenuResponse::getId)
			.collect(Collectors.toList());

		assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
		assertThat(responseIdList).containsAnyElementsOf(idList);
	}

	public static MenuResponse 메뉴가_생성_되어있음(MenuRequest menu) {
		return 메뉴_생성_요청(menu).as(MenuResponse.class);
	}

	public static void 메뉴_생성_실패됨(ExtractableResponse<Response> response, int status) {
		assertThat(response.statusCode()).isEqualTo(status);
	}

	public static MenuRequest 메뉴_생성_요청값_생성(String name, Integer price, Long menuGroupId,
		List<MenuProductRequest> menuProducts) {
		return MenuRequest.of(name, price, menuGroupId, menuProducts);
	}

	public static ExtractableResponse<Response> 메뉴_생성_요청(MenuRequest params) {
		return RestAssured.given().log().all()
			.contentType(MediaType.APPLICATION_JSON_VALUE)
			.body(params)
			.when()
			.post(MENU_PATH)
			.then().log().all()
			.extract();
	}

	public static void 메뉴_생성됨(ExtractableResponse<Response> response) {
		assertThat(response.statusCode()).isEqualTo(HttpStatus.CREATED.value());
	}

	public static List<MenuProductRequest> 메뉴_상품_요청_생성_되어_있음(ProductResponse product) {
		MenuProductRequest menuProductRequest = MenuProductRequest.of(product.getId(), 2L);
		return Collections.singletonList(menuProductRequest);
	}
}
