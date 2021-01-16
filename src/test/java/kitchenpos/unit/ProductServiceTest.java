package kitchenpos.unit;

import kitchenpos.product.application.ProductService;
import kitchenpos.product.domain.Price;
import kitchenpos.product.domain.Product;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
public class ProductServiceTest {

	private Product product;

	@Autowired
	private ProductService productService;

	@BeforeEach
	void setUp() {
		product = new Product("후라이드", new Price(16_000));
	}


	@Test
	@DisplayName("상품을 등록할 수 있다.")
	public void create() throws Exception {
		// when
		Product response = productService.create(product);

		// then
		assertThat(response).isNotNull();
		assertThat(response.getName()).isEqualTo(this.product.getName());
		assertThat(response.getPrice()).isEqualTo(this.product.getPrice());
	}
}
