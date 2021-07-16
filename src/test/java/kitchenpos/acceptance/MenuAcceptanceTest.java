package kitchenpos.acceptance;

import static org.assertj.core.api.Assertions.*;

import java.math.BigDecimal;
import java.util.Arrays;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import kitchenpos.domain.Menu;
import kitchenpos.domain.MenuGroup;
import kitchenpos.domain.MenuProduct;
import kitchenpos.domain.Product;

public class MenuAcceptanceTest extends AcceptanceTest {
	@DisplayName("메뉴 등록 및 조회 시나리오")
	@Test
	void createMenuAndFindMenuScenario() {
		// Backgroud
		// Given
		ExtractableResponse<Response> menuGroupResponse = RestAssured
			.given().log().all()
			.contentType(MediaType.APPLICATION_JSON_VALUE)
			.body(new MenuGroup("인기 메뉴"))
			.when().post("/api/menu-groups")
			.then().log().all()
			.extract();
		MenuGroup menuGroup = menuGroupResponse.as(MenuGroup.class);

		// And
		ExtractableResponse<Response> productResponse = RestAssured
			.given().log().all()
			.contentType(MediaType.APPLICATION_JSON_VALUE)
			.body(new Product("매운 라면", new BigDecimal(8000)))
			.when().post("/api/products")
			.then().log().all()
			.extract();
		Product product = productResponse.as(Product.class);

		// Scenario
		// When
		ExtractableResponse<Response> menuCreatedResponse = RestAssured
			.given().log().all()
			.contentType(MediaType.APPLICATION_JSON_VALUE)
			.body(new Menu("라면 메뉴", new BigDecimal(8000), menuGroup.getId(), Arrays.asList(new MenuProduct(product.getId(), 2L))))
			.when().post("/api/menus")
			.then().log().all()
			.extract();
		Menu createdMenu = menuCreatedResponse.as(Menu.class);

		// Then
		assertThat(menuCreatedResponse.statusCode()).isEqualTo(HttpStatus.CREATED.value());
		// When
		ExtractableResponse<Response> menuResponse = RestAssured
			.given().log().all()
			.contentType(MediaType.APPLICATION_JSON_VALUE)
			.when().get("/api/menus")
			.then().log().all()
			.extract();

		String menuName = menuResponse.jsonPath().getList(".", Menu.class).stream()
			.filter(menu -> menu.getId() == createdMenu.getId())
			.map(Menu::getName)
			.findFirst()
			.get()
			;

		assertThat(menuName).isEqualTo("라면 메뉴");
	}
}
