package kitchenpos.acceptance;

import static kitchenpos.acceptance.RestAssuredUtils.*;
import static kitchenpos.generator.MenuProductGenerator.*;
import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.List;

import org.springframework.http.HttpStatus;

import io.restassured.mapper.TypeRef;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import kitchenpos.domain.Menu;
import kitchenpos.domain.MenuGroup;
import kitchenpos.domain.Product;
import kitchenpos.generator.MenuGenerator;

public class MenuAcceptanceUtils {

	private MenuAcceptanceUtils() {
	}

	public static final String MENU_API_URL = "/api/menus";

	public static Menu 메뉴_등록_되어_있음(String name, BigDecimal price, Long groupMenuId, Long productId, Long quantity) {
		return 메뉴_등록_요청(name, price, groupMenuId, productId, quantity).as(Menu.class);
	}

	public static ExtractableResponse<Response> 메뉴_등록_요청(
		String name,
		BigDecimal price,
		Long menuGroupId,
		Long productId,
		Long quantity
	) {
		return post(MENU_API_URL, createRequest(name, price, menuGroupId, productId, quantity)).extract();
	}

	public static ExtractableResponse<Response> 메뉴_목록_조회_요청() {
		return get(MENU_API_URL).extract();
	}

	public static void 메뉴_등록_됨(ExtractableResponse<Response> response, String expectedName,
		BigDecimal expectedPrice, Long expectedQuantity, MenuGroup menuGroup, Product product) {
		Menu menu = response.as(Menu.class);
		assertAll(
			() -> assertThat(response.statusCode()).isEqualTo(HttpStatus.CREATED.value()),
			() -> assertThat(menu.getName()).isEqualTo(expectedName),
			() -> assertThat(menu.getPrice().intValue()).isEqualTo(expectedPrice.intValue()),
			() -> assertThat(menu.getMenuGroupId()).isEqualTo(menuGroup.getId()),
			() -> assertThat(menu.getMenuProducts())
				.first()
				.satisfies(menuProduct -> {
					assertThat(menuProduct.getQuantity()).isEqualTo(expectedQuantity);
					assertThat(menuProduct.getProductId()).isEqualTo(product.getId());
					assertThat(menuProduct.getMenuId()).isEqualTo(menu.getId());
				})
		);
	}

	public static void 메뉴_목록_조회_됨(ExtractableResponse<Response> response, Menu expectedMenu, MenuGroup menuGroup) {
		List<Menu> menus = response.as(new TypeRef<List<Menu>>() {
		});
		System.out.println("menus = " + menus);
		assertAll(
			() -> assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value()),
			() -> assertThat(response.jsonPath().getList(".", Menu.class))
				.first()
				.satisfies(menu -> {
					assertThat(menu.getName()).isEqualTo(expectedMenu.getName());
					assertThat(menu.getPrice().intValue()).isEqualTo(expectedMenu.getPrice().intValue());
					assertThat(menu.getMenuGroupId()).isEqualTo(menuGroup.getId());
				})
		);
	}

	private static Menu createRequest(String name, BigDecimal price, Long menuGroupId, Long productId, Long quantity) {
		return MenuGenerator.메뉴(name, price, menuGroupId,
			Collections.singletonList(메뉴_상품(1L, productId, quantity)));
	}
}
