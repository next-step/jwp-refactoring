package kitchenpos.product;

import static org.assertj.core.api.Assertions.*;

import java.math.BigDecimal;

import org.junit.jupiter.api.DisplayName;

import kitchenpos.menu.domain.Price;
import kitchenpos.product.domain.Product;

@DisplayName("상품 도메인 테스트")
public class ProductTest {

	void 상품_생성() {
		Product product = new Product(1L , "상품", new Price(new BigDecimal(1000)));
		assertThat(product).isNotNull();
	}
}
