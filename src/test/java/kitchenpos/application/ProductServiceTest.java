package kitchenpos.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatIllegalArgumentException;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import kitchenpos.dao.JdbcTemplateProductDao;
import kitchenpos.domain.Product;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class ProductServiceTest {

	@Autowired
	private ProductService productService;
	@Autowired
	private JdbcTemplateProductDao productDao;

	@DisplayName("금액이 0원 이상인 상품을 등록한다.")
	@Test
	void create() {
		//given
		Product product = new Product();
		product.setName("리코타치즈샐러드");
		product.setPrice(new BigDecimal(6_000));

		//when
		Product savedProduct = productService.create(product);
		//then
		상품이_등록됨(savedProduct);

		//when
		List<Product> list = productService.list();
		//then
		상품목록이_조회됨(list);
	}

	@DisplayName("금액이 없거나 0원미만인 상품은 등록할 수 없다.")
	@Test
	void createWithUnderZeroPrice() {
		//given
		Product product = new Product();
		product.setName("리코타치즈샐러드");
		product.setPrice(new BigDecimal(-1));
		//when
		상품등록이_실패함(product);

		//given
		product.setPrice(null);
		//when
		상품등록이_실패함(product);
	}

	private void 상품이_등록됨(Product savedProduct) {
		Optional<Product> actual = productDao.findById(savedProduct.getId());
		assertThat(actual.isPresent()).isTrue();
	}

	private void 상품목록이_조회됨(List<Product> list) {
		assertThat(list.isEmpty()).isFalse();
	}

	private void 상품등록이_실패함(Product product) {
		assertThatIllegalArgumentException()
			  .isThrownBy(() -> productService.create(product));
	}
}
