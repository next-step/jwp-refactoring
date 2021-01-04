package kitchenpos.application;

import static kitchenpos.common.TestFixture.*;
import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

import java.util.List;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullSource;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;

import kitchenpos.common.BaseTest;
import kitchenpos.common.TestDataUtil;
import kitchenpos.dao.ProductDao;
import kitchenpos.domain.Product;

@DisplayName("ProductService 테스트")
class ProductServiceTest extends BaseTest {

	@Autowired
	private ProductService productService;

	@Autowired
	private ProductDao productDao;

	@DisplayName("상품을 등록할 수 있다.")
	@Test
	void create() {

		Product product = productService.create(TestDataUtil.createProduct(예제_상품명, 예제_상품_가격));

		Product savedProduct = productDao.findById(product.getId()).orElse(null);
		assertAll(
			() -> assertThat(savedProduct.getId()).isNotNull(),
			() -> assertThat(savedProduct.getName()).isEqualTo(예제_상품명),
			() -> assertThat(savedProduct.getPrice().intValue()).isEqualTo(예제_상품_가격)
		);
	}

	@DisplayName("상품 가격을 기록하지 않거나, 0보다 작으면 등록할 수 없다.")
	@ParameterizedTest
	@NullSource
	@ValueSource(ints = {-1, -100, -1000})
	void createThrow(Integer price) {
		assertThatExceptionOfType(IllegalArgumentException.class)
			.isThrownBy(() -> {
				productService.create(TestDataUtil.createProduct(예제_상품명, price));
			});

	}

	@DisplayName("상품을 조회할 수 있다.")
	@Test
	void list() {
		List<Product> products = productService.list();

		assertThat(products).hasSize(6);
	}
}