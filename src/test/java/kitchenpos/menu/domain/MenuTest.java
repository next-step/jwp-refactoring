package kitchenpos.menu.domain;

import static org.assertj.core.api.Assertions.*;

import java.util.Arrays;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import kitchenpos.common.Price;
import kitchenpos.menu.domain.domain.Menu;
import kitchenpos.menu.domain.domain.MenuProduct;
import kitchenpos.menu.exception.InvalidMenuPriceException;
import kitchenpos.menugroup.domain.domain.MenuGroup;
import kitchenpos.product.domain.domain.Product;

class MenuTest {

	private final MenuGroup 논알콜메뉴그룹 = MenuGroup.of(1L, "논알콜");
	private final Product 콜라 = Product.of(1L, "콜라", 100);
	private final Product 사이다 = Product.of(2L, "사이다", 50);
	private final Product 요구르트 = Product.of(3L, "요구르트", 25);

	private final MenuProduct 콜라1개_100원 = MenuProduct.of(콜라, 1);
	private final MenuProduct 사이다2개_100원 = MenuProduct.of(사이다, 2);
	private final MenuProduct 요구르트2개_50원 = MenuProduct.of(요구르트, 2);

	@DisplayName("메뉴 가격와 상품들의 총 합산 가격이 같은가")
	@Test
	void validate_price_menu_products_total_price() {
		final Menu 음료메뉴_200원 = Menu.of("음료메뉴", 200, 논알콜메뉴그룹,
			Arrays.asList(콜라1개_100원, 사이다2개_100원));

		assertThat(음료메뉴_200원.getPrice()).isEqualTo(Price.of(200));
	}

	@DisplayName("메뉴 가격와 상품들의 총 합산 가격이 같은가: 다르면 예외발생")
	@Test
	void validate_price_menu_products_total_price_not_same() {
		assertThatExceptionOfType(InvalidMenuPriceException.class)
			.isThrownBy(() -> Menu.of("음료메뉴", 200, 논알콜메뉴그룹,
				Arrays.asList(요구르트2개_50원, 사이다2개_100원)
			));
	}
}
