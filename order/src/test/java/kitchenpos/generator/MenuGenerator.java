package kitchenpos.generator;

import java.math.BigDecimal;

import kitchenpos.order.menu.domain.Menu;
import kitchenpos.order.order.domain.Price;

public class MenuGenerator {

	private MenuGenerator() {
	}

	public static Menu 메뉴(long id, String name, BigDecimal price) {
		return Menu.of(id, name, Price.from(price));
	}

	public static Menu 후라이드_세트() {
		return 메뉴(
			1L,
			"후라이드 세트",
			BigDecimal.TEN);
	}
}
