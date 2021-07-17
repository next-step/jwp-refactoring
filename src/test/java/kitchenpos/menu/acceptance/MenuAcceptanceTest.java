package kitchenpos.menu.acceptance;

import static kitchenpos.menu.acceptance.MenuAcceptanceTestMethod.*;
import static kitchenpos.menugroup.acceptance.MenuGroupAcceptanceTestMethod.*;
import static kitchenpos.product.acceptance.ProductAcceptanceTestMethod.*;
import static org.assertj.core.api.Assertions.*;

import java.math.BigDecimal;
import java.util.Arrays;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import kitchenpos.AcceptanceTest;
import kitchenpos.menu.dto.MenuRequest;
import kitchenpos.menu.dto.MenuResponse;
import kitchenpos.menuGroup.dto.MenuGroupRequest;
import kitchenpos.menu.dto.MenuProductRequest;
import kitchenpos.menuGroup.dto.MenuGroupResponse;
import kitchenpos.product.dto.ProductRequest;
import kitchenpos.product.dto.ProductResponse;

public class MenuAcceptanceTest extends AcceptanceTest {
	@DisplayName("메뉴 등록 및 조회 시나리오")
	@Test
	void createMenuAndFindMenuScenario() {
		// Backgroud
		// Given
		ExtractableResponse<Response> menuGroupResponse = createMenuGroup(new MenuGroupRequest("인기 메뉴"));
		MenuGroupResponse menuGroup = menuGroupResponse.as(MenuGroupResponse.class);
		// And
		ExtractableResponse<Response> productResponse = createProduct(new ProductRequest("매운 라면", 8000L));
		ProductResponse product = productResponse.as(ProductResponse.class);

		// Scenario
		// When
		ExtractableResponse<Response> menuCreatedResponse = createMenu(new MenuRequest("라면 메뉴", new BigDecimal(8000), menuGroup.getId(), Arrays.asList(new MenuProductRequest(product.getId(), 2L))));
		MenuResponse createdMenu = menuCreatedResponse.as(MenuResponse.class);
		// Then
		assertThat(menuCreatedResponse.statusCode()).isEqualTo(HttpStatus.CREATED.value());

		// When
		ExtractableResponse<Response> menuResponse = findAllMenu();
		// Then
		String menuName = menuResponse.jsonPath().getList(".", MenuResponse.class).stream()
			.filter(menu -> menu.getId() == createdMenu.getId())
			.map(MenuResponse::getName)
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
		MenuGroupResponse menuGroup = menuGroupResponse.as(MenuGroupResponse.class);
		// And
		ExtractableResponse<Response> productResponse = createProduct(new ProductRequest("매운 라면", 8000L));
		ProductResponse product = productResponse.as(ProductResponse.class);

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
