package kitchenpos.menu.domain;

import static org.assertj.core.api.Assertions.*;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.assertj.core.api.ThrowableAssert.ThrowingCallable;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import kitchenpos.common.domain.Price;
import kitchenpos.common.domain.Quantity;
import kitchenpos.product.domain.Product;
import kitchenpos.product.domain.ProductName;

@DisplayName("메뉴 상품들")
class MenuProductsTest {

	@DisplayName("생성")
	@Test
	void of() {
		// given
		MenuProduct menuProduct1 = MenuProduct.of(
			Product.of(
				ProductName.of("강정치킨"),
				Price.of(BigDecimal.valueOf(17000))),
			Quantity.of(1L));
		MenuProduct menuProduct2 = MenuProduct.of(
			Product.of(
				ProductName.of("양념치킨"),
				Price.of(BigDecimal.valueOf(18000))),
			Quantity.of(2L));

		// when
		MenuProducts.of(Arrays.asList(menuProduct1, menuProduct2));
	}

	@DisplayName("생성 실패 - 한개 이상이어야 한다.")
	@Test
	void ofFailOnEmpty() {
		// given
		List<MenuProduct> empty = Collections.emptyList();

		// when
		ThrowingCallable throwingCallable = () -> MenuProducts.of(empty);

		// then
		assertThatThrownBy(throwingCallable).isInstanceOf(IllegalArgumentException.class);
	}

	@DisplayName("총 금액을 구할 수 있다.")
	@Test
	void getTotalPrice() {
		// given
		MenuProducts menuProducts = MenuProducts.of(Arrays.asList(
			MenuProduct.of(
				Product.of(
					ProductName.of("강정치킨"),
					Price.of(BigDecimal.valueOf(17000))),
				Quantity.of(1L)),
			MenuProduct.of(
				Product.of(
					ProductName.of("양념치킨"),
					Price.of(BigDecimal.valueOf(18000))),
				Quantity.of(2L))));

		// when
		Price actual = menuProducts.getTotalPrice();

		// then
		assertThat(actual).isEqualTo(Price.of(BigDecimal.valueOf(53000)));
	}
}
