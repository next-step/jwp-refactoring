package kitchenpos.application;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.BDDMockito.*;

import java.math.BigDecimal;
import java.util.Arrays;
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

	@DisplayName("상품을 등록할 수 있다.")
	@Test
	void create() {
		// given
		String name = "타코야끼";
		BigDecimal price = BigDecimal.valueOf(12000);
		Product product = new Product(name, price);

		given(productDao.save(any()))
			.willReturn(product);

		//when
		Product savedProduct = productService.create(product);

		//then
		assertThat(product.getName()).isEqualTo(savedProduct.getName());
	}

	@DisplayName("상품 가격은 0원 이상이어야 한다.")
	@Test
	void create_exception() {
		//given
		String name = "타코야끼";
		BigDecimal price = BigDecimal.valueOf(-1);

		//when
		Product product = new Product(name, price);

		//then
		assertThatThrownBy(() -> productService.create(product))
			.isInstanceOf(IllegalArgumentException.class);
	}

	@DisplayName("상품 목록을 조회할 수 있다.")
	@Test
	void list() {
		//given
		List<Product> products = Arrays.asList(new Product("타코야끼", BigDecimal.valueOf(12000)),
			new Product("뿌링클", BigDecimal.valueOf(22000)));
		given(productDao.findAll())
			.willReturn(products);

		//when
		List<Product> findProducts = productService.list();

		//then
		assertThat(findProducts.size()).isEqualTo(products.size());
		상품_목록_확인(findProducts, products);
	}

	private void 상품_목록_확인(List<Product> findProducts, List<Product> mockProducts) {
		List<String> findProductNames = findProducts.stream()
			.map(Product::getName)
			.collect(Collectors.toList());

		List<String> mockProductNames = mockProducts.stream()
			.map(Product::getName)
			.collect(Collectors.toList());
		assertThat(findProductNames).containsAll(mockProductNames);
	}
}
