package kitchenpos.generator;

import kitchenpos.common.domain.Quantity;
import kitchenpos.menu.domain.MenuProduct;

public class MenuProductGenerator {

	private MenuProductGenerator() {
	}

	public static MenuProduct 메뉴_상품(Long productId, long quantity) {
		return MenuProduct.of(productId, Quantity.from(quantity));
	}

	public static MenuProduct 후라이드_세트_상품() {
		return 메뉴_상품(1L, 1L);
	}

	public static MenuProduct 양념_세트_상품() {
		return 메뉴_상품(2L, 1L);
	}
}
