package kitchenpos.generator;

import java.math.BigDecimal;

import kitchenpos.common.domain.Price;
import kitchenpos.order.menu.domain.Menu;

public class MenuGenerator {

	private MenuGenerator() {
	}

	public static Menu 메뉴(String name, BigDecimal price) {
		return Menu.of(1L, name, Price.from(price));
	}

	public static Menu 후라이드_세트() {
		return 메뉴(
			"후라이드 세트",
			BigDecimal.TEN);
	}
}
