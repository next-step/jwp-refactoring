package kitchenpos.menu.domain.product;

import static org.assertj.core.api.Assertions.*;

import java.math.BigDecimal;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

@DisplayName("상품 도메인 테스트")
public class ProductTest {

	@DisplayName("상품 생성")
	@Test
	void create() {

		final String name = "강정치킨";
		final BigDecimal price = new BigDecimal(16000);
		final Product 강정치킨 = new Product(name, price);

		assertThat(강정치킨).isNotNull();
		assertThat(강정치킨.getName()).isEqualTo(name);
		assertThat(강정치킨.getPrice()).isEqualTo(price);

	}

}
