package kitchenpos.domain;

import static org.assertj.core.api.Assertions.*;

import java.math.BigDecimal;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class ProductTest {
	@DisplayName("가격이 음수인 상품은 생성 될 수 없다.")
	@Test
	void negativePriceTest() {
		assertThatThrownBy(() -> new Product("상품", Price.wonOf(BigDecimal.valueOf(-1))))
			.isInstanceOf(IllegalArgumentException.class)
			.hasMessageContaining("가격은 음수가 될 수 없습니다.");
	}
}