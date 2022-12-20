package kitchenpos.generator;

import static kitchenpos.generator.ProductGenerator.*;
import static org.mockito.Mockito.*;

import kitchenpos.common.domain.Quantity;
import kitchenpos.menu.domain.MenuProduct;
import kitchenpos.menu.domain.Product;

public class MenuProductGenerator {

	private MenuProductGenerator() {
	}

	public static MenuProduct 메뉴_상품(Product product, long quantity) {
		return MenuProduct.of(product, Quantity.from(quantity));
	}

	public static MenuProduct 후라이드_세트_상품() {
		MenuProduct menuProduct = spy(MenuProduct.of(양념_치킨(), Quantity.from(2L)));
		when(menuProduct.getSeq()).thenReturn(1L);
		when(menuProduct.getMenuId()).thenReturn(1L);
		return menuProduct;
	}

	public static MenuProduct 후라이드_세트_상품2() {
		MenuProduct menuProduct = spy(MenuProduct.of(양념_치킨(), Quantity.from(2L)));
		when(menuProduct.getSeq()).thenReturn(2L);
		when(menuProduct.getMenuId()).thenReturn(1L);
		return menuProduct;
	}

	public static MenuProduct 양념_세트_상품() {
		return 메뉴_상품(양념_치킨(), 1L);
	}
}
