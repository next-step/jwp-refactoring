package kitchenpos.menu.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.LongStream;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import kitchenpos.menu.application.ProductService;
import kitchenpos.menu.domain.Money;
import kitchenpos.menu.domain.Product;
import kitchenpos.menu.domain.ProductRepository;

@ExtendWith(MockitoExtension.class)
class ProductServiceTest {

	@Mock
	ProductRepository productRepository;

	@InjectMocks
	ProductService productService;

	@Test
	@DisplayName("상품목록 등록")
	void testCreateProduct() {
		Product product = new Product("스파게티", Money.valueOf(100));

		when(productRepository.save(any()))
			.thenReturn(product);

		Product createdProduct = productService.create(product);

		assertThat(product).isEqualTo(createdProduct);
		verify(productRepository, times(1)).save(product);
	}

	@Test
	@DisplayName("상품목록 목록")
	void testGetProductList() {
		// given
		List<Product> expectedProducts = createProductList(3);
		when(productRepository.findAll()).thenReturn(expectedProducts);

		// when
		List<Product> actualProducts = productService.findAll();

		// then
		assertThat(actualProducts).containsExactlyInAnyOrderElementsOf(expectedProducts);
		verify(productRepository, times(1)).findAll();
	}

	private List<Product> createProductList(int count) {
		return LongStream.range(0, count)
			.mapToObj(id -> new Product(id, "product-" + id, Money.valueOf(1000)))
			.collect(Collectors.toList());
	}

}
