package kitchenpos.generator;

import static kitchenpos.generator.MenuGroupGenerator.*;
import static kitchenpos.generator.MenuProductGenerator.*;
import static org.mockito.Mockito.*;

import java.math.BigDecimal;
import java.util.Arrays;

import kitchenpos.menu.domain.Menu;
import kitchenpos.menu.domain.MenuGroup;
import kitchenpos.menu.domain.MenuProducts;
import kitchenpos.menu.domain.Price;

public class MenuGenerator {

	private MenuGenerator() {
	}

	public static Menu 메뉴(String name, Price price, MenuGroup menuGroup, MenuProducts menuProducts) {
		return Menu.of(name, price, menuGroup, menuProducts);
	}

	public static Menu 후라이드_세트() {
		Menu 후라이드_세트 = spy(메뉴("후라이드 세트", Price.from(BigDecimal.valueOf(16000)), 한마리_메뉴(), MenuProducts.from(
			Arrays.asList(후라이드_세트_상품(), 양념_세트_상품()))));
		lenient().when(후라이드_세트.getId()).thenReturn(1L);
		return 후라이드_세트;
	}
}
