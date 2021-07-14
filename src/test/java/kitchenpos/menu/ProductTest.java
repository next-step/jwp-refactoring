package kitchenpos.menu;

import static org.assertj.core.api.Assertions.*;

import java.math.BigDecimal;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import kitchenpos.menu.domain.Price;
import kitchenpos.menu.domain.Product;

@DisplayName("상품 도메인 테스트")
public class ProductTest {

	@DisplayName("상품 생성 테스트")
	@Test
	void 상품_생성() {
		Product product = new Product(1L , "상품", new Price(BigDecimal.valueOf(1000)));
		assertThat(product).isNotNull();
		assertThat(product.getId()).isEqualTo(1L);
		assertThat(product.getName()).isEqualTo("상품");
		assertThat(product.getPrice().value()).isEqualTo(BigDecimal.valueOf(1000));
	}
}
