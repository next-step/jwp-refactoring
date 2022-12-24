package ktichenpos.menu.generator;

import static ktichenpos.menu.generator.MenuGroupGenerator.*;
import static ktichenpos.menu.generator.MenuProductGenerator.*;
import static org.mockito.Mockito.*;

import java.math.BigDecimal;
import java.util.Collections;

import kitchenpos.common.domain.Price;
import ktichenpos.menu.menu.domain.Menu;
import ktichenpos.menu.menu.domain.MenuGroup;
import ktichenpos.menu.menu.domain.MenuProducts;

public class MenuGenerator {

	private MenuGenerator() {
	}

	public static Menu 메뉴(String name, Price price, MenuGroup menuGroup, MenuProducts menuProducts) {
		return Menu.of(name, price, menuGroup, menuProducts);
	}

	public static Menu 후라이드_세트() {
		Menu 후라이드_세트 = spy(메뉴(
			"후라이드 세트",
			Price.from(BigDecimal.TEN),
			한마리_메뉴(),
			MenuProducts.from(
				Collections.singletonList(후라이드_세트_상품()))));
		lenient().when(후라이드_세트.id()).thenReturn(1L);
		return 후라이드_세트;
	}
}
