package kitchenpos.domain;

import static org.assertj.core.api.Assertions.*;

import java.math.BigDecimal;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class PriceTest {

	@DisplayName("가격은 음수가 될 수 없다.")
	@Test
	void negativePriceTest() {
		assertThatThrownBy(() -> Price.wonOf(BigDecimal.valueOf(-1)))
			.isInstanceOf(IllegalArgumentException.class)
			.hasMessageContaining("가격은 음수가 될 수 없습니다.");
	}

}