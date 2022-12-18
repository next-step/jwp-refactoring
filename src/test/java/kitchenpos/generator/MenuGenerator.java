package kitchenpos.generator;

import static kitchenpos.generator.MenuGroupGenerator.*;
import static kitchenpos.generator.MenuProductGenerator.*;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.List;

import kitchenpos.menu.domain.Menu;
import kitchenpos.menu.domain.MenuProduct;

public class MenuGenerator {

	private MenuGenerator() {
	}

	public static Menu 메뉴(String name, BigDecimal price, Long menuGroupId, List<MenuProduct> menuProducts) {
		return new Menu(1L, name, price, menuGroupId, menuProducts);
	}

	public static Menu 후라이드_세트() {
		return 메뉴("후라이드 세트", BigDecimal.valueOf(16000), 메뉴_그룹("반반메뉴").getId(), Collections.singletonList(후라이드_세트_상품()));
	}
}
