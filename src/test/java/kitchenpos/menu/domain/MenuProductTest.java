package kitchenpos.menu.domain;

import static kitchenpos.product.ProductFixture.*;
import static org.assertj.core.api.Assertions.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import kitchenpos.common.domain.Quantity;
import kitchenpos.product.domain.Product;

@DisplayName("메뉴 상품")
public class MenuProductTest {

	@DisplayName("생성")
	@Test
	void of() {
		// given
		Product product = 강정치킨_상품();
		Quantity quantity = Quantity.from(1L);

		// when
		MenuProduct menuProduct = MenuProduct.of(product.getId(), quantity);

		// then
		assertThat(menuProduct.getProductId()).isEqualTo(product.getId());
		assertThat(menuProduct.getQuantity()).isEqualTo(quantity);
	}
}
