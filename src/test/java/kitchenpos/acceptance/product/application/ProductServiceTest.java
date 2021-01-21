package kitchenpos.acceptance.product.application;

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

import kitchenpos.product.application.ProductService;
import kitchenpos.product.domain.Product;
import kitchenpos.product.domain.ProductRepository;
import kitchenpos.product.dto.ProductRequest;
import kitchenpos.product.dto.ProductResponse;

@DisplayName("상품 Stubbing 테스트")
@ExtendWith(MockitoExtension.class)
class ProductServiceTest {

	@Mock
	private ProductRepository productRepository;

	private ProductService productService;

	@BeforeEach
	void setUp() {
		productService = new ProductService(productRepository);
	}

	@DisplayName("상품: 상품 생성 테스트")
	@Test
	void createTest() {
		// given
		ProductRequest request = ProductRequest.of("맥주", 5000);
		given(productRepository.save(any())).willReturn(request.toProduct());

		// when
		ProductResponse result = productService.create(request);

		// then
		assertAll(
			() -> assertThat(result).isNotNull(),
			() -> assertThat(result.getName()).isEqualTo(request.getName())
		);
	}

	@DisplayName("상품: 상품 가격은 0보다 커야한다.")
	@Test
	void priceErrorTest() {
		// given // when
		ProductRequest request = ProductRequest.of("맥주", -5000);

		// then
		assertThatIllegalArgumentException().isThrownBy(() -> productService.create(request));
	}

	@DisplayName("상품: 상품 목록 조회 테스트")
	@Test
	void findAllProductsTest() {
		// given
		Product product1 = ProductRequest.of("국밥", 7000).toProduct();
		Product product2 = ProductRequest.of("설렁탕", 9000).toProduct();
		Product product3 = ProductRequest.of("갈비탕", 16000).toProduct();
		given(productRepository.findAll()).willReturn(Arrays.asList(product1, product2, product3));

		// when
		List<ProductResponse> result = productService.list();

		// then
		assertAll(
			() -> assertThat(result).isNotNull(),
			() -> assertThat(result).hasSize(3)
		);
	}
}
