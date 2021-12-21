package kitchenpos.application;

import static kitchenpos.common.DomainFixture.*;
import static org.assertj.core.api.Assertions.*;
import static org.mockito.BDDMockito.*;

import java.math.BigDecimal;
import java.util.Arrays;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import kitchenpos.dao.ProductDao;
import kitchenpos.domain.Product;

@ExtendWith(MockitoExtension.class)
class ProductServiceTest {

	@InjectMocks
	private ProductService productService;

	@Mock
	private ProductDao productDao;

	@Test
	void create() {
		final Product 딤섬 = product(1L, "딤섬", 5_000);
		given(productDao.save(any())).willReturn(딤섬);

		final Product createdProduct = productService.create(product(null, "딤섬", 5_000));

		assertThat(createdProduct.getId()).isNotNull();
	}

	@Test
	void create_invalid_price() {
		final BigDecimal nullPrice = null;
		assertThatIllegalArgumentException()
			.isThrownBy(() -> productService.create(product(null, "만두", nullPrice)));

		final long minusPrice = -1;
		assertThatIllegalArgumentException()
			.isThrownBy(() -> productService.create(product(null, "탕수육", minusPrice)));
	}

	@Test
	void list() {
		final Product 짜장 = product(1L, "짜장", 7_000);
		final Product 짬뽕 = product(2L, "짬뽕", 9_000);
		given(productDao.findAll()).willReturn(Arrays.asList(짜장, 짬뽕));

		assertThat(productService.list()).containsExactly(짜장, 짬뽕);
	}
}
