package kitchenpos.menu;

import static kitchenpos.product.ProductFixture.*;

import kitchenpos.common.domain.Quantity;
import kitchenpos.menu.domain.MenuProduct;

public class MenuProductFixture {
	public static MenuProduct 후라이드치킨_2개_메뉴_상품() {
		return MenuProduct.of(1L, 후라이드치킨_상품().getId(), Quantity.from(2L));
	}

	public static MenuProduct 강정치킨_메뉴_상품() {
		return MenuProduct.of(2L, 강정치킨_상품().getId(), Quantity.from(1L));
	}

	public static MenuProduct 양념치킨_메뉴_상품() {
		return MenuProduct.of(3L, 양념치킨_상품().getId(), Quantity.from(1L));
	}
}
