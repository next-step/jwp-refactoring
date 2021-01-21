package kitchenpos.application;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.*;

import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import kitchenpos.dao.ProductDao;
import kitchenpos.domain.Product;

@DisplayName("상품 Stubbing 테스트")
@ExtendWith(MockitoExtension.class)
class ProductServiceTest {

	@Mock
	private ProductDao productDao;

	private ProductService productService;

	@BeforeEach
	void setUp() {
		productService = new ProductService(productDao);
	}

	@DisplayName("상품: 상품 생성 테스트")
	@Test
	void createTest() {
		// given
		Product product = Product.of(1L, "맥주", 5000);
		given(productDao.save(any())).willReturn(product);

		// when
		Product result = productService.create(product);

		// then
		assertAll(
			() -> assertThat(result).isNotNull(),
			() -> assertThat(result).isEqualTo(product)
		);
	}

	@DisplayName("상품: 상품 가격은 0보다 커야한다.")
	@Test
	void priceErrorTest() {
		// given // when
		Product product = Product.of(null, "맥주", -5000);

		// then
		assertThatIllegalArgumentException().isThrownBy(() -> productService.create(product));
	}

	@DisplayName("상품: 상품 목록 조회 테스트")
	@Test
	void findAllProductsTest() {
		// given
		Product product1 = Product.of(1L, "국밥", 7000);
		Product product2 = Product.of(2L, "설렁탕", 9000);
		Product product3 = Product.of(3L, "갈비탕", 16000);
		given(productDao.findAll()).willReturn(Arrays.asList(product1, product2, product3));

		// when
		List<Product> result = productService.list();

		// then
		assertAll(
			() -> assertThat(result).isNotNull(),
			() -> assertThat(result).hasSize(3)
		);
	}
}
