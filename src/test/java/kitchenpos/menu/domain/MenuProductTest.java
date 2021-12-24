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
		Product product = Product.of(Name.from("강정치킨"), Price.from(BigDecimal.valueOf(17000)));
		Quantity quantity = Quantity.from(2L);

		// when
		MenuProduct menuProduct = MenuProduct.of(product, quantity);

		// then
		assertThat(menuProduct.getProduct()).isEqualTo(product);
		assertThat(menuProduct.getQuantity()).isEqualTo(quantity);
	}

	@DisplayName("총 금액을 구할 수 있다.")
	@Test
	void getTotalPrice() {
		// given
		MenuProduct menuProduct = MenuProduct.of(
			Product.of(
				Name.from("강정치킨"),
				Price.from(BigDecimal.valueOf(17000))),
			Quantity.from(2L));

		// when
		Price price = menuProduct.getTotalPrice();

		// then
		assertThat(price).isEqualTo(Price.from(BigDecimal.valueOf(34000)));
	}
}
