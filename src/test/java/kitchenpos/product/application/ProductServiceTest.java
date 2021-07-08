package kitchenpos.product.application;

import static java.util.Arrays.*;
import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.List;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import kitchenpos.product.domain.Name;
import kitchenpos.product.domain.ProductRepository;
import kitchenpos.product.domain.Product;
import kitchenpos.product.dto.ProductRequest;
import kitchenpos.product.dto.ProductResponse;
import kitchenpos.product.application.ProductService;

@DisplayName("상품 요구사항 테스트")
@ExtendWith(MockitoExtension.class)
class ProductServiceTest {

	@Mock
	private ProductRepository productRepository;

	@InjectMocks
	private ProductService productService;

	@DisplayName("상품을 등록할 수 있다.")
	@Test
	void createProductTest() {
		// given
		ProductRequest productRequest = mock(ProductRequest.class);
		Product product = mock(Product.class);
		when(product.getName()).thenReturn(Name.valueOf("mock product"));
		when(productRequest.toEntity()).thenReturn(product);
		when(productRepository.save(any(Product.class))).thenReturn(product);

		// when
		productService.create(productRequest);

		// than
		verify(productRepository).save(any(Product.class));
	}

	@DisplayName("상품 목록을 조회할 수 있다.")
	@Test
	void listTest() {
		// given
		Product product = mock(Product.class);
		when(product.getId()).thenReturn(1L);
		when(product.getName()).thenReturn(Name.valueOf("mock product"));
		when(productRepository.findAll()).thenReturn(asList(product));

		// when
		List<ProductResponse> productResponses = productService.findAll();

		// then
		assertThat(productResponses).isNotEmpty();
		assertThat(productResponses.get(0).getId()).isEqualTo(1L);
	}
}
