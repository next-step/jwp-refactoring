package kitchenpos.menu.domain;

import static org.assertj.core.api.Assertions.*;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
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
			.jsonPath().getList(".", Menu.class)
			.stream()
			.map(Menu::getId)
			.collect(Collectors.toList());

		assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
		assertThat(responseIdList).containsAnyElementsOf(idList);
	}

	public static Menu 메뉴가_생성_되어있음(Menu menu) {
		return 메뉴_생성_요청(menu).as(Menu.class);
	}

	public static void 메뉴_생성_실패됨(ExtractableResponse<Response> response) {
		assertThat(response.statusCode()).isEqualTo(HttpStatus.INTERNAL_SERVER_ERROR.value());
	}

	public static Menu 메뉴_생성_요청값_생성(String name, Integer price, Long menuGroupId, List<MenuProduct> menuProductList) {
		Menu menu = new Menu();
		menu.setName(name);
		if (price != null) {
			menu.setPrice(new BigDecimal(price));
		}
		menu.setMenuGroupId(menuGroupId);
		menu.setMenuProducts(menuProductList);
		return menu;
	}

	public static ExtractableResponse<Response> 메뉴_생성_요청(Menu params) {
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

	public static List<MenuProduct> 메뉴_상품_생성되어_있음(ProductResponse product) {
		MenuProduct menuProduct = new MenuProduct();
		menuProduct.setProductId(product.getId());
		menuProduct.setQuantity(2L);

		return Collections.singletonList(menuProduct);
	}
}
