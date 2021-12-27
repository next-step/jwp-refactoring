package kitchenpos.product.application;

import static org.assertj.core.api.Assertions.*;

import java.math.BigDecimal;
import java.util.List;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import kitchenpos.common.Price;
import kitchenpos.product.domain.Product;
import kitchenpos.product.dto.ProductRequest;

@Transactional
@SpringBootTest
class ProductServiceTest {
	private static final String NAME = "로제치킨";
	private static final BigDecimal PRICE = new BigDecimal(23000);

	@Autowired
	private ProductService productService;

	@Test
	@DisplayName("상품 생성 테스트")
	public void createProductSuccessTest() {
		//given
		//when
		Product product = productService.create(new ProductRequest(NAME, PRICE));
		//then
		assertThat(product).isEqualTo(new Product(product.getId(), NAME, Price.valueOf(PRICE)));
	}

	@Test
	@DisplayName("상품가격이 0보다 작아서 생성 실패 테스트")
	public void createProductFailTest() {
		assertThatThrownBy(() -> productService.create(new ProductRequest(NAME, new BigDecimal(-1))))
			.isInstanceOf(IllegalArgumentException.class)
			.hasMessage("상품 가격은 0이상의 값을 가져야합니다.");
	}

	@Test
	@DisplayName("상품 조회 테스트")
	public void findProductListTest() {
		//given
		//when
		List<Product> products = productService.list();
		//then
		assertThat(products).hasSize(6);
	}

}
