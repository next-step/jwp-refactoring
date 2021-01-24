package kitchenpos.application;

import static kitchenpos.TestFixtures.*;
import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.*;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import kitchenpos.dao.ProductDao;
import kitchenpos.domain.Product;

@DisplayName("상품 BO 테스트")
@ExtendWith(MockitoExtension.class)
class ProductServiceTest {

	@Mock
	private ProductDao productDao;

	@InjectMocks
	private ProductService productService;

	@DisplayName("상품 생성")
	@Test
	void create_happyPath() {
		// given
		Product 새_상품 = new Product.Builder().name("새상품").price(BigDecimal.valueOf(4000L)).build();
		given(productDao.save(새_상품)).willAnswer(invocation -> {
			새_상품.setId(1L);
			return 새_상품;
		});

		// when
		Product persistProduct = productService.create(새_상품);

		// then
		assertAll(
			() -> assertThat(persistProduct.getId()).isEqualTo(1L),
			() -> assertThat(persistProduct.getName()).isEqualTo(새_상품.getName()),
			() -> assertThat(persistProduct.getPrice()).isEqualTo(새_상품.getPrice())
		);
	}

	@DisplayName("상품 생성 : 가격이 0원보다 작음")
	@Test
	void create_exceptionCase() {
		// given
		Product 새_상품 = new Product.Builder().name("새상품").price(BigDecimal.valueOf(-1L)).build();

		// when & then
		assertThatThrownBy(() -> productService.create(새_상품)).isInstanceOf(IllegalArgumentException.class);
	}

	@DisplayName("상품 목록 조회")
	@Test
	void list() {
		// given
		given(productDao.findAll()).willReturn(Arrays.asList(상품1, 상품2));

		// when
		List<Product> persistProductList = productService.list();

		// then
		assertThat(persistProductList)
			.hasSize(2)
			.anySatisfy(product -> {
				assertThat(product.getId()).isEqualTo(상품1.getId());
				assertThat(product.getName()).isEqualTo(상품1.getName());
				assertThat(product.getPrice()).isEqualTo(상품1.getPrice());
			})
			.anySatisfy(product -> {
				assertThat(product.getId()).isEqualTo(상품2.getId());
				assertThat(product.getName()).isEqualTo(상품2.getName());
				assertThat(product.getPrice()).isEqualTo(상품2.getPrice());
			});
	}
}
