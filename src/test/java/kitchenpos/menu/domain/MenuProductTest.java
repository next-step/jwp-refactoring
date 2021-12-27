package kitchenpos.menu.domain;

import static org.assertj.core.api.Assertions.*;

import java.math.BigDecimal;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import kitchenpos.common.Price;
import kitchenpos.menu.domain.domain.MenuProduct;
import kitchenpos.product.domain.domain.Product;

class MenuProductTest {

	@DisplayName("메뉴 상품의 총 합산 가격 구하기")
	@Test
	void getTotalPrice() {
		final Product 만두 = Product.of("만두", BigDecimal.valueOf(500));
		final MenuProduct 만두2개 = MenuProduct.of(만두, 2L);

		assertThat(만두2개.getTotalPrice()).isEqualTo(Price.of(1_000));
	}
}
