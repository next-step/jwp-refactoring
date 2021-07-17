package kitchenpos.application;

import kitchenpos.dao.ProductRepository;
import kitchenpos.domain.Product;
import org.assertj.core.util.Lists;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ProductServiceTest {
	@Mock
	private ProductRepository productRepository;

	@InjectMocks
	private ProductService productService;

	@Test
	void createProductTest() {
		Product product = new Product("피자", BigDecimal.valueOf(10000));
		when(productRepository.save(product)).thenReturn(product);
		assertThat(productService.create(product)).isNotNull();
	}

	@Test
	@DisplayName("상품 생성 시 상품의 가격이 없거나 0보다 작으면 익셉션 발생")
	void createProductFailTest() {
		Product inputProduct = new Product("피자", BigDecimal.valueOf(-10));
		assertThatThrownBy(() -> productService.create(inputProduct))
				.isInstanceOf(IllegalArgumentException.class);

		Product inputProduct2 = new Product("피자", null);
		assertThatThrownBy(() -> productService.create(inputProduct2))
				.isInstanceOf(IllegalArgumentException.class);
	}

	@Test
	void getProductListTest() {
		when(productRepository.findAll()).thenReturn(Lists.list(new Product(), new Product()));
		assertThat(productService.list()).hasSize(2);
	}
}
