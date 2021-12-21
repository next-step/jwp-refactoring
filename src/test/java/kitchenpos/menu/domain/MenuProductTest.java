package kitchenpos.menu.domain;

import static org.assertj.core.api.Assertions.*;

import java.math.BigDecimal;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import kitchenpos.common.domain.Name;
import kitchenpos.common.domain.Price;
import kitchenpos.common.domain.Quantity;
import kitchenpos.product.domain.Product;

@DisplayName("메뉴 상품")
public class MenuProductTest {

	@DisplayName("생성")
	@Test
	void of() {
		// given
		Product product = Product.of(Name.of("강정치킨"), Price.of(BigDecimal.valueOf(17000)));
		Quantity quantity = Quantity.of(2L);

		// when
		MenuProduct menuProduct = MenuProduct.of(product, quantity);

		// then
		assertThat(menuProduct.getProduct()).isEqualTo(product);
		assertThat(menuProduct.getQuantity()).isEqualTo(quantity);
	}
}
