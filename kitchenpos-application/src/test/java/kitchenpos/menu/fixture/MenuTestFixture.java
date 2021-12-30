package kitchenpos.menu.fixture;

import java.math.BigDecimal;
import java.util.Collections;

import kitchenpos.menu.domain.Menu;
import kitchenpos.menu.domain.MenuGroup;
import kitchenpos.menu.domain.MenuProduct;
import kitchenpos.menu.domain.MenuProducts;
import kitchenpos.product.fixture.ProductTestFixture;

public class MenuTestFixture {

	public static final MenuGroup 추천메뉴 = MenuGroup.of(1L, "추천메뉴");
	public static final Menu 후라이드둘 = Menu.of(1L, "후라이드둘", BigDecimal.valueOf(30_000), 추천메뉴);
	public static final MenuProduct 후라이드둘_메뉴_후라이드_상품 = MenuProduct.of(1L, 후라이드둘, ProductTestFixture.후라이드.getId(), 2L);
	public static final MenuProducts 후라이드둘_메뉴_후라이드_상품들 = MenuProducts.of(
		Collections.singletonList(후라이드둘_메뉴_후라이드_상품));

}
