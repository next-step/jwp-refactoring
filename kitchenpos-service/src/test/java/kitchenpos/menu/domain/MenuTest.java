package kitchenpos.menu.domain;

import kitchenpos.product.domain.Product;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.Arrays;

class MenuTest {
	@Test
	@DisplayName("포함된 상품 가격의 총 합이 입력 가격보다 작으면 익셉션 발생")
	void addMenuProductFailTest() {
		Menu menu = new Menu(1L, "치킨", BigDecimal.valueOf(20000), 1L);
		Product product = new Product(1L, "치킨", BigDecimal.valueOf(10000));
		MenuProduct menuProduct = new MenuProduct(menu, product, 1);

		Assertions.assertThatThrownBy(() -> menu.addMenuProduct(Arrays.asList(menuProduct)))
				.isInstanceOf(IllegalArgumentException.class);
	}
}
