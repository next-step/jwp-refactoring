package kitchenpos.generator;

import java.math.BigDecimal;
import java.util.List;

import kitchenpos.menu.domain.Menu;
import kitchenpos.menu.domain.MenuProduct;

public class MenuGenerator {

	private MenuGenerator() {
	}

	public static Menu 메뉴(String name, BigDecimal price, Long menuGroupId, List<MenuProduct> menuProducts) {
		return new Menu(1L, name, price, menuGroupId, menuProducts);
	}
}
