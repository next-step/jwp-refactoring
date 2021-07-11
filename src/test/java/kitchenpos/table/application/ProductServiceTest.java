package kitchenpos.table.application;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import kitchenpos.menu.application.ProductService;
import kitchenpos.menu.domain.Price;
import kitchenpos.menu.domain.Product;
import kitchenpos.menu.dto.ProductRequest;
import kitchenpos.menu.dto.ProductResponse;
import kitchenpos.menu.repository.ProductRepository;

@ExtendWith(MockitoExtension.class)
class ProductServiceTest {

	@Mock
	private ProductRepository productRepository;

	@InjectMocks
	private ProductService productService;

	@DisplayName("상품 생성 테스트")
	@Test
	void testCreateProduct() {
		Price price = new Price(BigDecimal.valueOf(2000));
		ProductRequest productRequest = new ProductRequest("상품1", BigDecimal.valueOf(2000));
		Product product = new Product(productRequest.getName(), new Price(productRequest.getPrice()));
		Product psavedProduct = new Product(1L, "상품1", price);

		when(productRepository.save(product)).thenReturn(psavedProduct);
		ProductResponse actual = productService.create(productRequest);

		assertThat(actual.getPrice()).isEqualTo(psavedProduct.getPrice().getPrice());
	}

	@DisplayName("상품 가격이 0보다 작으면 생성 오류")
	@Test
	void testPriceUnderZero() {
		ProductRequest productRequest = new ProductRequest("상품1", BigDecimal.valueOf(-1));

		assertThatThrownBy(() -> {
			productService.create(productRequest);
		}).isInstanceOf(IllegalArgumentException.class)
			.hasMessageContaining("상품 가격은 0보다 작을 수 없습니다.");
	}

	@DisplayName("상품 목록 반환 테스트")
	@Test
	void testList() {
		List<Product> products = new ArrayList<>();
		Price price = new Price(BigDecimal.valueOf(2000));
		products.add(new Product(1L, "상품1", price));
		products.add(new Product(2L, "상품2", price));
		products.add(new Product(3L, "상품3", price));

		when(productRepository.findAll()).thenReturn(products);

		List<ProductResponse> actual = productService.list();

		List<Long> actualProductIds = actual.stream().map(ProductResponse::getId).collect(Collectors.toList());
		List<Long> expectedProductIds = products.stream().map(Product::getId).collect(Collectors.toList());
		assertThat(actualProductIds).containsExactlyElementsOf(expectedProductIds);
	}
}