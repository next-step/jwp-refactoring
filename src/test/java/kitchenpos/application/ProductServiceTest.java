package kitchenpos.application;

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

import kitchenpos.dao.ProductDao;
import kitchenpos.domain.Product;

@ExtendWith(MockitoExtension.class)
class ProductServiceTest {

	@Mock
	private ProductDao productDao;

	@InjectMocks
	private ProductService productService;

	@DisplayName("상품 생성 테스트")
	@Test
	void testCreateProduct() {
		//given
		Product product = mock(Product.class);
		Product savedProduct = new Product(1L, "상품1", BigDecimal.valueOf(2000));
		//when
		when(product.getPrice()).thenReturn(BigDecimal.valueOf(2000));
		when(productDao.save(eq(product))).thenReturn(savedProduct);
		Product actual = productService.create(product);
		//then
		assertThat(actual.getPrice()).isEqualTo(savedProduct.getPrice());
	}

	@DisplayName("상품 가격이 0보다 작으면 생성 오류")
	@Test
	void testPriceUnderZero() {
		Product product = mock(Product.class);
		//when
		when(product.getPrice()).thenReturn(BigDecimal.valueOf(-1));

		verify(productDao, never()).save(eq(product));
		assertThatThrownBy(() -> {
			productService.create(product);
		}).isInstanceOf(IllegalArgumentException.class)
			.hasMessageContaining("상품 가격은 0보다 작을 수 없습니다.");
	}

	@DisplayName("상품 목록 반환 테스트")
	@Test
	void testList() {
		List<Product> products = new ArrayList<>();
		products.add(new Product(1L, "상품1", BigDecimal.valueOf(2000)));
		products.add(new Product(2L, "상품2", BigDecimal.valueOf(2000)));
		products.add(new Product(3L, "상품3", BigDecimal.valueOf(2000)));

		when(productDao.findAll()).thenReturn(products);

		List<Product> actual = productService.list();

		assertThat(actual.stream().map(Product::getId).collect(Collectors.toList()))
			.containsExactlyElementsOf(products.stream().map(Product::getId).collect(Collectors.toList()));
	}
}