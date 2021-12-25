package kitchenpos.menu.domain;

import static org.assertj.core.api.Assertions.*;

import java.math.BigDecimal;
import java.util.Arrays;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import kitchenpos.common.InvalidPriceException;
import kitchenpos.common.Price;
import kitchenpos.menu.exception.InvalidMenuPriceException;
import kitchenpos.menugroup.domain.MenuGroup;
import kitchenpos.product.domain.Product;

class MenuTest {

	@DisplayName("메뉴 가격와 상품들의 총 합산 가격 비교")
	@Test
	void validate_price_menu_products() {
		final MenuGroup 논알콜메뉴그룹 = MenuGroup.of(1L, "논알콜");
		final Product 콜라 = Product.of("콜라", BigDecimal.valueOf(100));
		final MenuProduct 콜라1개_100원 = MenuProduct.of(콜라, 1);
		final Product 사이다 = Product.of("사이다", BigDecimal.valueOf(50));
		final MenuProduct 사이다2개_100원 = MenuProduct.of(사이다, 2);
		final Product 요구르트 = Product.of("요구르트", BigDecimal.valueOf(25));
		final MenuProduct 요구르트2개_50원 = MenuProduct.of(요구르트, 2);

		final BigDecimal 음료메뉴_200원 = BigDecimal.valueOf(200);
		final Menu 음료메뉴 = Menu.of("음료메뉴", 음료메뉴_200원, 논알콜메뉴그룹,
			Arrays.asList(콜라1개_100원, 사이다2개_100원));

		assertThat(음료메뉴.getPrice()).isEqualTo(Price.of(200));

		assertThatExceptionOfType(InvalidMenuPriceException.class)
			.isThrownBy(() -> Menu.of("음료메뉴", 음료메뉴_200원, 논알콜메뉴그룹,
				Arrays.asList(요구르트2개_50원, 사이다2개_100원
			)));
	}
}
