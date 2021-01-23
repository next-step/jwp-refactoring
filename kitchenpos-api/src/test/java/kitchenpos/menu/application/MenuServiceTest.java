package kitchenpos.menu.application;

import static kitchenpos.utils.TestFixture.*;
import static org.assertj.core.api.Assertions.*;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import kitchenpos.BaseServiceTest;
import kitchenpos.menu.dto.MenuProductRequest;
import kitchenpos.menu.dto.MenuRequest;
import kitchenpos.menu.dto.MenuResponse;

public class MenuServiceTest extends BaseServiceTest {
	@Autowired
	private MenuService menuService;

	private MenuProductRequest menuProductRequest1;
	private MenuProductRequest menuProductRequest2;
	private List<MenuProductRequest> menuProductRequests;

	@BeforeEach
	public void setUp() {
		menuProductRequest1 = new MenuProductRequest(메뉴상품_신규_1_후라이드_ID, 메뉴상품_신규_1_후라이드_QUANTITY);
		menuProductRequest2 = new MenuProductRequest(메뉴상품_신규_2_양념_ID, 메뉴상품_신규_2_양념_QUANTITY);
		menuProductRequests = Arrays.asList(menuProductRequest1, menuProductRequest2);
	}

	@Test
	@DisplayName("메뉴를 등록할 수 있다.")
	void create() {
		//given
		MenuRequest menuRequest = new MenuRequest(메뉴_신규_NAME, 메뉴_신규_PRICE, 메뉴_신규_MENU_GROUP_ID, menuProductRequests);

		//when
		MenuResponse result = menuService.create(menuRequest);

		//then
		assertThat(result.getId()).isNotNull();
		assertThat(result.getName()).isEqualTo(메뉴_신규_NAME);
		assertThat(result.getPrice()).isEqualByComparingTo(메뉴_신규_PRICE);
		assertThat(result.getMenuGroupId()).isEqualTo(메뉴_신규_MENU_GROUP_ID);
		assertThat(result.getMenuProducts().size()).isEqualTo(2);
		assertThat(result.getMenuProducts().get(0).getMenuId()).isEqualTo(result.getId());
		assertThat(result.getMenuProducts().get(0).getProductId()).isEqualTo(메뉴상품_신규_1_후라이드_ID);
		assertThat(result.getMenuProducts().get(1).getMenuId()).isEqualTo(result.getId());
		assertThat(result.getMenuProducts().get(1).getProductId()).isEqualTo(메뉴상품_신규_2_양념_ID);
	}

	@Test
	@DisplayName("메뉴 등록 시, 메뉴의 가격이 없으면 IllegalArgumentException을 throw 해야한다.")
	void createPriceNull() {
		//given
		MenuRequest menuRequest = new MenuRequest(메뉴_신규_NAME, null, 메뉴_신규_MENU_GROUP_ID, menuProductRequests);

		//when-then
		assertThatThrownBy(() -> menuService.create(menuRequest))
			.isInstanceOf(IllegalArgumentException.class);
	}

	@Test
	@DisplayName("메뉴 등록 시, 메뉴 그룹이 등록되어있지 않으면 IllegalArgumentException을 throw 해야한다.")
	void createNotExistMenuGroup() {
		//given
		MenuRequest notExistMenuGroupMenu = new MenuRequest(메뉴_신규_NAME, 메뉴_신규_PRICE, 존재하지않는_ID, menuProductRequests);

		//when-then
		assertThatThrownBy(() -> menuService.create(notExistMenuGroupMenu))
			.isInstanceOf(IllegalArgumentException.class);
	}

	@Test
	@DisplayName("메뉴 등록 시, 상품이 등록되어있지 않으면 IllegalArgumentException을 throw 해야한다.")
	void createNotExistProduct() {
		//given
		MenuProductRequest notExistProduct = new MenuProductRequest(존재하지않는_ID, 1);
		MenuRequest notExistProductMenu = new MenuRequest(메뉴_신규_NAME, 메뉴_신규_PRICE, 메뉴_신규_MENU_GROUP_ID, Arrays.asList(notExistProduct));

		//when-then
		assertThatThrownBy(() -> menuService.create(notExistProductMenu))
			.isInstanceOf(IllegalArgumentException.class);
	}

	@Test
	@DisplayName("메뉴 등록 시, 메뉴의 가격이 상품 가격의 합보다 크면 IllegalArgumentException을 throw 해야한다.")
	void createPriceLessThanZero() {
		//given
		BigDecimal greaterThanSum = 메뉴상품_신규_가격_총합.add(BigDecimal.valueOf(10000));
		MenuRequest greaterThanSumOfProductPriceMenu = new MenuRequest(메뉴_신규_NAME, greaterThanSum, 메뉴_신규_MENU_GROUP_ID, menuProductRequests);

		//when-then
		assertThatThrownBy(() -> menuService.create(greaterThanSumOfProductPriceMenu))
			.isInstanceOf(IllegalArgumentException.class);
	}

	@Test
	@DisplayName("메뉴의 목록을 메뉴의 상품목록과 함께 조회할 수 있다.")
	void findAll() {
		//when
		List<MenuResponse> results = menuService.findAll();

		//then
		assertThat(results).isNotEmpty();
		assertThat(results.get(0).getMenuProducts()).isNotEmpty();
		assertThat(results.get(0).getMenuProducts().get(0).getProductId()).isNotNull();
	}
}
