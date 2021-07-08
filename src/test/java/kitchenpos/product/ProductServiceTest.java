package kitchenpos.product;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.BDDMockito.*;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import kitchenpos.dao.ProductDao;
import kitchenpos.menu.domain.Price;
import kitchenpos.product.application.ProductService;
import kitchenpos.product.domain.Product;

@DisplayName("상품 서비스 테스트")
@ExtendWith(MockitoExtension.class)
public class ProductServiceTest {

	@Mock
	private ProductDao productDao;

	@InjectMocks
	private ProductService productService;

	Product 양념치킨;

	@BeforeEach
	void setUp() {
		양념치킨 = ProductServiceTest.상품생성(1L, new BigDecimal(10000), "양념 치킨");
	}

	@DisplayName("상품을 생성한다.")
	@Test
	void 상품_생성한다() {
		given(productDao.save(양념치킨)).willReturn(양념치킨);

		Product created = productService.create(양념치킨);

		상품_생성_확인(created, 양념치킨);
	}

	@DisplayName("상품 생성 시 - 상품의 가격이 Null이면 생성할 수 없다.")
	@Test
	void 상품_생성_시_상품의_가격이_NULL_이면_생성할_수_없다() {
		양념치킨 = ProductServiceTest.상품생성(1L, null, "양념 치킨");
		assertThatThrownBy(() -> {
			productService.create(양념치킨);
		}).isInstanceOf(IllegalArgumentException.class);
	}

	@DisplayName("상품 생성 시 - 상품의 가격이 0원 이상이여야 한다")
	@Test
	void 상품_생성_시_상품의_가격이_0원_초과하여야_한다() {
		양념치킨 = ProductServiceTest.상품생성(1L, new BigDecimal(-5), "양념 치킨");
		assertThatThrownBy(() -> {
			productService.create(양념치킨);
		}).isInstanceOf(IllegalArgumentException.class);
	}

	@DisplayName("상품의 리스트를 조회한다.")
	@Test
	void 상품의_리스트를_조회한다() {
		given(productDao.findAll()).willReturn(Arrays.asList(양념치킨));

		List<Product> selected = productService.list();

		상품_리스트_조회_확인(selected, Arrays.asList(양념치킨));
	}

	private void 상품_리스트_조회_확인(List<Product> selected, List<Product> expected) {
		assertThat(selected).containsAll(expected);
	}

	private void 상품_생성_확인(Product created, Product expected) {
		assertThat(created.getId()).isEqualTo(expected.getId());
		assertThat(created.getName()).isEqualTo(expected.getName());
		assertThat(created.getPrice()).isEqualTo(expected.getPrice());
	}

	public static Product 상품생성(Long id, BigDecimal price, String name) {
		Product product = new Product(id, name, new Price(price));
		return product;
	}
}
