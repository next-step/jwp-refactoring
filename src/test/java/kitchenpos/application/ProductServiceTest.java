package kitchenpos.application;

import kitchenpos.product.application.ProductService;
import kitchenpos.product.domain.Product;
import kitchenpos.product.domain.ProductRepository;
import kitchenpos.product.dto.ProductRequest;
import org.assertj.core.util.Lists;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ProductServiceTest {
	@Mock
	private ProductRepository productRepository;

	@InjectMocks
	private ProductService productService;

	@Test
	void createProductTest() {
		ProductRequest productRequest = new ProductRequest("피자", BigDecimal.valueOf(10000));

		when(productRepository.save(productRequest.toProduct())).thenReturn(productRequest.toProduct());
		assertThat(productService.create(productRequest)).isNotNull();
	}

	@Test
	void getProductListTest() {
		when(productRepository.findAll()).thenReturn(Lists.list(new Product(), new Product()));
		assertThat(productService.list()).hasSize(2);
	}
}
