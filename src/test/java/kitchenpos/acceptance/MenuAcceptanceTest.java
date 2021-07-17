package kitchenpos.acceptance;

import static kitchenpos.acceptance.MenuAcceptanceTestMethod.*;
import static kitchenpos.acceptance.MenuGroupAcceptanceTestMethod.*;
import static kitchenpos.acceptance.ProductAcceptanceTestMethod.*;
import static org.assertj.core.api.Assertions.*;

import java.math.BigDecimal;
import java.util.Arrays;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import kitchenpos.menu.dto.MenuRequest;
import kitchenpos.menuGroup.dto.MenuGroupRequest;
import kitchenpos.menu.dto.MenuProductRequest;
import kitchenpos.product.dto.ProductRequest;

public class MenuAcceptanceTest extends AcceptanceTest {
	@DisplayName("메뉴 등록 및 조회 시나리오")
	@Test
	void createMenuAndFindMenuScenario() {
		// Backgroud
		// Given
		ExtractableResponse<Response> menuGroupResponse = createMenuGroup(new MenuGroupRequest("인기 메뉴"));
		MenuGroupRequest menuGroup = menuGroupResponse.as(MenuGroupRequest.class);
		// And
		ExtractableResponse<Response> productResponse = createProduct(new ProductRequest("매운 라면", new BigDecimal(8000)));
		ProductRequest product = productResponse.as(ProductRequest.class);

		// Scenario
		// When
		ExtractableResponse<Response> menuCreatedResponse = createMenu(new MenuRequest("라면 메뉴", new BigDecimal(8000), menuGroup.getId(), Arrays.asList(new MenuProductRequest(product.getId(), 2L))));
		MenuRequest createdMenu = menuCreatedResponse.as(MenuRequest.class);
		// Then
		assertThat(menuCreatedResponse.statusCode()).isEqualTo(HttpStatus.CREATED.value());

		// When
		ExtractableResponse<Response> menuResponse = findAllMenu();
		// Then
		String menuName = menuResponse.jsonPath().getList(".", MenuRequest.class).stream()
			.filter(menu -> menu.getId() == createdMenu.getId())
			.map(MenuRequest::getName)
			.findFirst()
			.get();
		assertThat(menuName).isEqualTo("라면 메뉴");
	}

	@DisplayName("메뉴 오류 시나리오")
	@Test
	void menuErroScenario() {
		// Backgroud
		// Given
		ExtractableResponse<Response> menuGroupResponse = createMenuGroup(new MenuGroupRequest("인기 메뉴"));
		MenuGroupRequest menuGroup = menuGroupResponse.as(MenuGroupRequest.class);
		// And
		ExtractableResponse<Response> productResponse = createProduct(new ProductRequest("매운 라면", new BigDecimal(8000)));
		ProductRequest product = productResponse.as(ProductRequest.class);

		// Scenario
		// When
		ExtractableResponse<Response> minusPriceResponse = createMenu(new MenuRequest("라면 메뉴", new BigDecimal(-8000), menuGroup.getId(), Arrays.asList(new MenuProductRequest(product.getId(), 2L))));
		// Then
		assertThat(minusPriceResponse.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value());

		// When
		ExtractableResponse<Response> notExistsProductResponse = createMenu(new MenuRequest("라면 메뉴", new BigDecimal(8000), menuGroup.getId(), Arrays.asList(new MenuProductRequest(0L, 2L))));
		// Then
		assertThat(notExistsProductResponse.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value());

		// When
		ExtractableResponse<Response> menuPriceIsBiggerThanProductSumResponse = createMenu(new MenuRequest("라면 메뉴", new BigDecimal(1000000), menuGroup.getId(), Arrays.asList(new MenuProductRequest(product.getId(), 2L))));
		// Then
		assertThat(menuPriceIsBiggerThanProductSumResponse.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value());
	}
}
