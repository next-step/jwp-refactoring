package kitchenpos.application;

import kitchenpos.MockitoTest;
import kitchenpos.dao.ProductDao;
import kitchenpos.domain.Product;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.times;


class ProductServiceTest extends MockitoTest {

	@InjectMocks
	private ProductService productService;

	@Mock
	private ProductDao productDao;

	@DisplayName("상품을 생성한다.")
	@Test
	void create() {
		// given
		Product product = MockFixture.productForCreate("제품", 1000);

		// when
		productService.create(product);

		// then
		Mockito.verify(productDao, times(1)).save(product);
	}

	@DisplayName("잘못된 가격으로 상품 생성시 실패.")
	@ParameterizedTest
	@ValueSource(longs = {-1, -999999999})
	void create_PriceException(long price) {
		// given
		Product product = MockFixture.productForCreate("제품", price);

		// when then
		assertThatThrownBy(() -> productService.create(product)).isInstanceOf(IllegalArgumentException.class);
	}

	@DisplayName("상품 리스트를 반환한다.")
	@ParameterizedTest
	@ValueSource(ints = {0, 1, 999})
	void list(int size) {
		// given
		List<Product> products = MockFixture.anyProducts(size);
		given(productDao.findAll()).willReturn(products);

		// when then
		assertThat(productService.list()).hasSize(size);
	}
}
