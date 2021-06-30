package kitchenpos.application;

import static java.util.Arrays.*;
import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.math.BigDecimal;
import java.util.List;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import kitchenpos.dao.ProductDao;
import kitchenpos.domain.Price;
import kitchenpos.domain.Product;

@DisplayName("상품 요구사항 테스트")
@ExtendWith(MockitoExtension.class)
class ProductServiceTest {

	@Mock
	private ProductDao productDao;

	@InjectMocks
	private ProductService productService;

	@DisplayName("상품을 등록할 수 있다.")
	@Test
	void createProductTest() {
		// given
		Product product = new Product("치킨", Price.wonOf(1000));

		// when
		productService.create(product);

		// than
		verify(productDao).save(product);
	}

	@DisplayName("상품 목록을 조회할 수 있다.")
	@Test
	void listTest() {
		// given
		Product product = mock(Product.class);
		when(productDao.findAll()).thenReturn(asList(product));

		// when
		List<Product> products = productService.list();

		// then
		assertThat(products).containsExactly(product);
	}
}
