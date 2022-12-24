package ktichenpos.menu.generator;

import static ktichenpos.menu.generator.ProductGenerator.*;
import static org.mockito.Mockito.*;

import ktichenpos.menu.menu.domain.MenuProduct;
import ktichenpos.menu.menu.domain.Product;
import ktichenpos.menu.menu.domain.Quantity;

public class MenuProductGenerator {

	private MenuProductGenerator() {
	}

	public static MenuProduct 메뉴_상품(Product product, long quantity) {
		return MenuProduct.of(product, Quantity.from(quantity));
	}

	public static MenuProduct 후라이드_세트_상품() {
		MenuProduct menuProduct = spy(MenuProduct.of(후라이드_치킨(), Quantity.from(2L)));
		lenient().when(menuProduct.seq()).thenReturn(1L);
		return menuProduct;
	}

	public static MenuProduct 후라이드_세트_상품2() {
		MenuProduct menuProduct = spy(MenuProduct.of(양념_치킨(), Quantity.from(2L)));
		when(menuProduct.seq()).thenReturn(2L);
		return menuProduct;
	}

	public static MenuProduct 양념_세트_상품() {
		return 메뉴_상품(양념_치킨(), 1L);
	}
}
