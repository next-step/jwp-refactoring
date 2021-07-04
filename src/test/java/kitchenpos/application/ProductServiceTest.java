package kitchenpos.application;

import kitchenpos.dao.ProductDao;
import kitchenpos.domain.Product;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.math.BigDecimal;
import java.util.List;

import static org.assertj.core.api.Assertions.*;

@SpringBootTest
class ProductServiceTest {

	@Autowired
	private ProductDao productDao;

	@Autowired
	private ProductService productService;

	@Test
	public void 상품_등록_성공() {
		Product product = new Product();

		String productName = "강정치킨";
		BigDecimal productPrice = new BigDecimal("17000");

		product.setName(productName);
		product.setPrice(productPrice);

		Product savedProduct = productService.create(product);

		assertThat(savedProduct.getName()).isEqualTo(productName);
		assertThat(savedProduct.getPrice()).isEqualByComparingTo(productPrice);
	}

	@Test
	public void 상품_등록_실패_상품금액_0원_불가() {
		Product product = new Product();

		String productName = "공짜치킨";
		BigDecimal productPrice = new BigDecimal("0");

		product.setName(productName);
		product.setPrice(productPrice);

		assertThatThrownBy(() -> productService.create(product))
				.isInstanceOf(IllegalArgumentException.class);
	}

	@Test
	public void 상품_등록_실패_상품금액_null원_불가() {
		Product product = new Product();

		String productName = "널널한치킨";
		BigDecimal productPrice = null;

		product.setName(productName);
		product.setPrice(productPrice);

		assertThatThrownBy(() -> productService.create(product))
				.isInstanceOf(IllegalArgumentException.class);
	}

	@Test
	public void 상품_목록_조회() {
		Product product = new Product();

		String productName = "강정치킨";
		BigDecimal productPrice = new BigDecimal("17000.00");

		product.setName(productName);
		product.setPrice(productPrice);

		productService.create(product);

		List<Product> products = productService.list();

		assertThat(products)
				.extracting("name", "price")
				.contains(tuple(productName, productPrice));
	}
}