package kitchenpos.menu.domain;

import static org.assertj.core.api.Assertions.*;

import java.math.BigDecimal;

import org.assertj.core.util.Lists;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import kitchenpos.common.domain.Price;
import kitchenpos.common.domain.Quantity;
import kitchenpos.menugroup.domain.MenuGroup;
import kitchenpos.product.domain.Product;

class MenuTest {

	private static Product product = new Product(1L, "양념치킨", Price.valueOf(new BigDecimal(15000)));
	private static MenuProduct menuProduct = new MenuProduct(null, 1L, Quantity.valueOf(2L));
	private static MenuGroup menuGroup = new MenuGroup(1L, "신메뉴");
	private static MenuProducts menuProducts = new MenuProducts(Lists.newArrayList(menuProduct));

	@Test
	@DisplayName("메뉴 생성 테스트")
	public void createMenuTest() {
		//when
		Menu menu = new Menu(1L, "신양념", Price.valueOf(new BigDecimal(20000)), 1L, menuProducts );

		//then
		assertThat(menu).isNotNull();
	}

	@Test
	@DisplayName("메뉴가격이 0보다 작아서 메뉴생성 실패")
	public void crateMenuFailPriceLessThanZeroTest() {
		assertThatThrownBy(
			() -> new Menu(1L, "신양념", Price.valueOf(new BigDecimal(-1)), 1L, menuProducts))
			.isInstanceOf(IllegalArgumentException.class)
			.hasMessage("가격은 0보다 작을 수 없습니다");
	}
}
