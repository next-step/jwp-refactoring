package kitchenpos.domain;

import kitchenpos.application.MenuGroupService;
import kitchenpos.application.ProductService;
import kitchenpos.dao.MenuGroupDao;
import kitchenpos.dao.ProductDao;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
public class ProductTest {

	@Mock
	private ProductDao productDao;

	private ProductService productService;

	@BeforeEach
	void setUp() {
		productService = new ProductService(productDao);
		assertThat(productService).isNotNull();
	}

	@Test
	void 상품을_등록한다() {
		Product product = new Product();
		product.setPrice(BigDecimal.valueOf(10000));

		when(productDao.save(product)).thenReturn(product);
		assertThat(productService.create(product)).isEqualTo(product);

	}

	@Test
	void 상품_목록을_조회한다() {
		when(productDao.findAll()).thenReturn(new ArrayList<>(Arrays.asList(new Product())));
		assertThat(productService.list()).isNotNull();
		assertThat(productService.list()).isNotEmpty();
		assertThat(productService.list().size()).isEqualTo(1);
	}

}
