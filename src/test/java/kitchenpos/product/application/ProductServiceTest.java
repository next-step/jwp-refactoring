package kitchenpos.product.application;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.BDDMockito.*;

import java.math.BigDecimal;
import java.util.Arrays;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import kitchenpos.product.domain.domain.Product;
import kitchenpos.product.domain.repo.ProductRepository;
import kitchenpos.product.dto.ProductAddRequest;
import kitchenpos.product.dto.ProductResponse;

@ExtendWith(MockitoExtension.class)
class ProductServiceTest {

	@InjectMocks
	private ProductService productService;

	@Mock
	private ProductRepository productRepository;

	@DisplayName("상품 생성")
	@Test
	void create() {
		final Product 딤섬 = Product.of(1L, "딤섬", 5_000);
		given(productRepository.save(any())).willReturn(딤섬);

		final ProductResponse createdProduct = productService.create(
			ProductAddRequest.of("딤섬", BigDecimal.valueOf(5_000))
		);

		assertThat(createdProduct.getId()).isNotNull();
	}

	@DisplayName("상품 목록 조회")
	@Test
	void list() {
		final Product 짜장 = Product.of(1L, "짜장", 7_000);
		final Product 짬뽕 = Product.of(2L, "짬뽕", 9_000);
		given(productRepository.findAll()).willReturn(Arrays.asList(짜장, 짬뽕));

		assertThat(productService.list().size()).isEqualTo(2);
	}
}
