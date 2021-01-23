package kitchenpos.unit;

import kitchenpos.application.product.ProductService;
import kitchenpos.dto.product.ProductRequest;
import kitchenpos.dto.product.ProductResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.math.BigDecimal;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
public class ProductServiceTest {

	private ProductRequest productRequest;

	@Autowired
	private ProductService productService;

	@BeforeEach
	void setUp() {
		productRequest = new ProductRequest("후라이드", BigDecimal.valueOf(16_000));
	}


	@Test
	@DisplayName("상품을 등록할 수 있다.")
	public void createProduct() {
		// when
		ProductResponse response = productService.createProduct(productRequest);

		// then
		assertThat(response).isNotNull();
		assertThat(response.getName()).isEqualTo(this.productRequest.getName());
		assertThat(response.getPrice()).isEqualTo(this.productRequest.getPrice());
	}

	@Test
	@DisplayName("상품 목록을 조회한다")
	public void listProduct() {
		List<ProductResponse> responses =  productService.listProducts();
		assertThat(responses).isNotNull();
		assertThat(responses.size()).isEqualTo(6);
	}
}
