package kitchenpos.application;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import kitchenpos.dao.ProductDao;
import kitchenpos.domain.Product;

@ExtendWith(MockitoExtension.class)
public class ProductServiceTest {
	@Mock
	private ProductDao productDao;

	@InjectMocks
	private ProductService productService;

	@Test
	void createTestInHappyCase() {
		// given
		when(productDao.save(any())).thenReturn(new Product("치킨", new BigDecimal(10000)));
		// when
		Product product = productService.create(new Product("치킨", new BigDecimal(10000)));
		// then
		assertThat(product.getName()).isEqualTo("치킨");
	}

	@Test
	void listTestInHappyCase() {
		// given
		when(productDao.findAll()).thenReturn(Arrays.asList(new Product(), new Product()));
		// when
		List<Product> products = productService.list();
		// then
		assertThat(products.size()).isEqualTo(2);
	}
}
