package kitchenpos.application;

import static kitchenpos.domain.DomainFactory.*;
import static org.assertj.core.api.Assertions.*;

import java.util.List;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;

import kitchenpos.domain.Product;

@SpringBootTest
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
class ProductServiceTest {
	@Autowired
	private ProductService productService;

	@DisplayName("가격이 0 보다 작으면 IllegalArgumentException 발생")
	@Test
	void create_ThrowIllegalArgumentException() {
		Product product = createProduct("강정치킨", -1);

		assertThatIllegalArgumentException()
			.isThrownBy(() -> productService.create(product));
	}

	@DisplayName("상품 등록")
	@Test
	void create() {
		Product product = createProduct("강정치킨", 17000);

		Product resultProduct = productService.create(product);

		assertThat(resultProduct.getId()).isNotNull();
	}

	@DisplayName("상품 목록 조회")
	@Test
	void list() {
		List<Product> resultProducts = productService.list();

		assertThat(resultProducts).hasSize(6);
	}
}