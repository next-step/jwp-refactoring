package kitchenpos.application;

import static kitchenpos.common.TestFixture.*;
import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Stream;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.NullSource;
import org.springframework.beans.factory.annotation.Autowired;

import kitchenpos.common.BaseTest;
import kitchenpos.domain.Product;
import kitchenpos.dto.ProductRequest;
import kitchenpos.dto.ProductResponse;
import kitchenpos.exception.WrongPriceException;
import kitchenpos.repository.ProductRepository;

@DisplayName("ProductService 테스트")
class ProductServiceTest extends BaseTest {

	@Autowired
	private ProductService productService;

	@Autowired
	private ProductRepository productRepository;

	@DisplayName("상품을 등록할 수 있다.")
	@Test
	void create() {

		ProductResponse product = productService.create(ProductRequest.of(예제_상품명, 예제_상품_가격));

		Product savedProduct = productRepository.findById(product.getId()).orElse(null);
		assertAll(
			() -> assertThat(savedProduct.getId()).isNotNull(),
			() -> assertThat(savedProduct.getName()).isEqualTo(예제_상품명),
			() -> assertThat(savedProduct.getPrice()).isEqualTo(예제_상품_가격)
		);
	}

	@DisplayName("상품 가격을 기록하지 않거나, 0보다 작으면 등록할 수 없다.")
	@ParameterizedTest
	@NullSource
	@MethodSource("paramCreateThrow")
	void createThrow(BigDecimal price) {
		assertThatExceptionOfType(WrongPriceException.class)
			.isThrownBy(() -> {
				productService.create(ProductRequest.of(예제_상품명, price));
			});

	}

	public static Stream<Arguments> paramCreateThrow() {
		return Stream.of(
			Arguments.of(BigDecimal.valueOf(-1)),
			Arguments.of(BigDecimal.valueOf(-10)),
			Arguments.of(BigDecimal.valueOf(-100)),
			Arguments.of(BigDecimal.valueOf(-1000)),
			Arguments.of(BigDecimal.valueOf(-10000))
		);
	}

	@DisplayName("상품을 조회할 수 있다.")
	@Test
	void list() {
		List<ProductResponse> products = productService.findAll();

		assertThat(products).hasSize(6);
	}
}