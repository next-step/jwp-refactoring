package kitchenpos.application.mock;

import static kitchenpos.domain.DomainFactory.*;
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

import kitchenpos.application.ProductService;
import kitchenpos.dao.ProductDao;
import kitchenpos.domain.Product;

@ExtendWith(MockitoExtension.class)
class ProductServiceTest {
	@InjectMocks
	private ProductService productService;
	@Mock
	private ProductDao productDao;

	@DisplayName("가격이 0 보다 작으면 IllegalArgumentException 발생")
	@Test
	void create_ThrowIllegalArgumentException() {
		assertThatIllegalArgumentException()
			.isThrownBy(() -> productService.create(createProduct("강정치킨", new BigDecimal(-1))));
	}

	@DisplayName("상품 등록")
	@Test
	void create() {
		String name = "강정치킨";
		BigDecimal price = new BigDecimal(17000);

		when(productDao.save(any(Product.class))).thenAnswer(invocation -> {
			Product product = invocation.getArgument(0, Product.class);
			product.setId(1L);
			return product;
		});

		Product resultProduct = productService.create(createProduct(name, price));

		assertThat(resultProduct.getId()).isNotNull();
		assertThat(resultProduct.getName()).isEqualTo(name);
		assertThat(resultProduct.getPrice()).isEqualTo(price);
	}

	@DisplayName("상품 목록 조회")
	@Test
	void list() {
		when(productDao.findAll()).thenReturn(createProducts(1L, 2L, 3L, 4L));

		List<Product> resultProducts = productService.list();

		assertThat(resultProducts).hasSize(4);
	}
}
