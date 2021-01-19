package kitchenpos.menu.domain;

import static org.assertj.core.api.Assertions.assertThat;

import java.math.BigDecimal;
import kitchenpos.product.domain.Product;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.test.util.ReflectionTestUtils;

class MenuProductTest {

	@DisplayName("메뉴상품의 금액 계산")
	@Test
	void sumOfPrice() {
		//given
		Product product = new Product("상품", new BigDecimal(500));
		MenuProduct menuProduct = new MenuProduct();
		ReflectionTestUtils.setField(menuProduct, "product", product);
		ReflectionTestUtils.setField(menuProduct, "quantity", 2L);

		//when, then
		assertThat(menuProduct.sumOfPrice()).isEqualTo(new BigDecimal(1_000));
	}
}
