package kitchenpos.generator;

import java.math.BigDecimal;
import java.util.List;

import kitchenpos.domain.Menu;
import kitchenpos.domain.MenuProduct;

public class MenuGenerator {

	private MenuGenerator() {
	}

	public static Menu 메뉴(String name, BigDecimal price, Long menuGroupId, List<MenuProduct> menuProducts) {
		return new Menu(1L, name, price, menuGroupId, menuProducts);
	}
}
