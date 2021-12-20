package kitchenpos.domain;

import static org.assertj.core.api.Assertions.*;

import java.math.BigDecimal;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class ProductTest{

	@Test
	@DisplayName("상품 생성 테스트")
	public void createProductTest() {
		//when
		Product product = new Product();
		product.setPrice(new BigDecimal(3000));
		product.setName("김밥");

		//then
		assertThat(product).isNotNull();
		assertThat(product.getName()).isEqualTo("김밥");
		assertThat(product.getPrice()).isEqualTo(new BigDecimal(3000));
	}

}
