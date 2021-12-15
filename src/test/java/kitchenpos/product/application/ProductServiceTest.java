package kitchenpos.product.application;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.BDDMockito.*;

import java.util.Arrays;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import kitchenpos.product.domain.Product;
import kitchenpos.product.domain.ProductRepository;
import kitchenpos.product.dto.ProductRequest;
import kitchenpos.product.exception.ProductException;

@DisplayName("상품 : 서비스 테스트")
@ExtendWith({MockitoExtension.class})
class ProductServiceTest {

	@Mock
	private ProductRepository productRepository;

	@InjectMocks
	private ProductService productService;

	private Product product;

	@DisplayName("상품 생성 테스트")
	@Test
	void createTest() {
		// given
		ProductRequest productRequest = ProductRequest.of("후라이드", 16000);

		// when
		when(productRepository.save(any(Product.class))).thenReturn(product);

		// then
		assertThat(productService.create(productRequest)).isEqualTo(product);
	}

	@DisplayName("상품의 가격이 없는 경우 생성시 예외처리 테스트")
	@Test
	void createPriceNull() {
		// given
		ProductRequest productRequest = ProductRequest.of("후라이드", null);

		// when // then
		assertThatThrownBy(() -> {
			productService.create(productRequest);
		}).isInstanceOf(ProductException.class);
	}

	@DisplayName("상품의 가격이 0보다 작은 경우 생성시 예외처리 테스트")
	@Test
	void createPriceUnderZero() {
		// given
		ProductRequest productRequest = ProductRequest.of("후라이드", -16000);

		// when // then
		assertThatThrownBy(() -> {
			productService.create(productRequest);
		}).isInstanceOf(ProductException.class);
	}

	@DisplayName("상품 목록 조회 테스트")
	@Test
	void getList() {
		// given
		product = Product.of("후라이드", 16000);
		Product anotherProduct = Product.of("양념치킨", 16000);

		// when
		when(productRepository.findAll()).thenReturn(Arrays.asList(product, anotherProduct));

		// then
		assertThat(productService.list()).containsExactly(product, anotherProduct);
	}
}
