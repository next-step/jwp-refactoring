package kitchenpos.generator;

import kitchenpos.domain.MenuProduct;

public class MenuProductGenerator {

	private MenuProductGenerator() {
	}

	public static MenuProduct 메뉴_상품(Long menuId, Long productId, long quantity) {
		return new MenuProduct(1L, menuId, productId, quantity);
	}
}
