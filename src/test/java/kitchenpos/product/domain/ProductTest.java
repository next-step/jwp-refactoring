package kitchenpos.product.domain;

import static org.assertj.core.api.Assertions.*;

import java.math.BigDecimal;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

@DisplayName("상품")
class ProductTest {

	@DisplayName("생성")
	@Test
	void of() {
		// given
		ProductName name = ProductName.of("강정치킨");
		ProductPrice price = ProductPrice.of(BigDecimal.valueOf(17000));

		// when
		Product product = Product.of(name, price);

		// then
		assertThat(product.getName()).isEqualTo(name);
		assertThat(product.getPrice()).isEqualTo(price);
	}
}
