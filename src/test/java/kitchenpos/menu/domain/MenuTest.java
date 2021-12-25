package kitchenpos.menu.domain;

import static org.assertj.core.api.Assertions.*;

import java.math.BigDecimal;

import org.assertj.core.util.Lists;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import kitchenpos.menu.dto.MenuProductRequest;
import kitchenpos.menu.dto.MenuRequest;
import kitchenpos.menugroup.domain.MenuGroup;
import kitchenpos.product.domain.Product;

class MenuTest {

	private static Product product = new Product(1L, "양념치킨", new BigDecimal(15000));
	private static MenuProduct menuProduct = new MenuProduct(null, product, 2);
	private static MenuGroup menuGroup = new MenuGroup(1L, "신메뉴");

	@Test
	@DisplayName("메뉴 생성 테스트")
	public void createMenuTest() {
		//when
		Menu menu = new Menu(1L,"신양념", new BigDecimal(20000), menuGroup, Lists.newArrayList(menuProduct));

		//then
		assertThat(menu).isNotNull();
	}

	@Test
	@DisplayName("메뉴가격이 0보다 작아서 메뉴생성 실패")
	public void crateMenuFailPriceLessThanZeroTest() {
		assertThatThrownBy(
			() -> new Menu(1L, "신양념", new BigDecimal(-1), menuGroup, Lists.newArrayList(menuProduct)))
			.isInstanceOf(IllegalArgumentException.class)
			.hasMessage("메뉴의 가격은 0보다 작을 수 없습니다");
	}

	@Test
	@DisplayName("메뉴가격이 상품들 가격합보다 커서 메뉴생성 실패")
	public void crateMenuFailPriceLessThanProductsPriceSumTest() {
		assertThatThrownBy(
			() -> new Menu(1L, "신양념", new BigDecimal(50000), menuGroup, Lists.newArrayList(menuProduct)))
			.isInstanceOf(IllegalArgumentException.class)
			.hasMessage("메뉴의 가격은 상품들의 가격합보다 작거나 같아야 합니다");
	}

}
