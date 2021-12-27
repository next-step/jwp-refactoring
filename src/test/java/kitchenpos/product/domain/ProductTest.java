package kitchenpos.product.domain;

import static org.assertj.core.api.Assertions.*;

import java.math.BigDecimal;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import kitchenpos.common.domain.Name;
import kitchenpos.common.domain.Price;

@DisplayName("제품 도메인 테스트")
public class ProductTest {

	@DisplayName("생성 테스트")
	@Test
	void createTest() {
		Long id = 1L;
		Name name = Name.of("후라이드");
		Price price = Price.of(BigDecimal.valueOf(15_000));

		assertThat(Product.of(id, name, price)).isEqualTo(Product.of(id, name, price));
	}

}
