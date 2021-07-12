package kitchenpos.domain.vo;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class QuantityTest {

	@DisplayName("수량은 음수가 될 수 없다.")
	@Test
	void negativeTest() {
		Assertions.assertThatThrownBy(() -> Quantity.of(-1))
			.isInstanceOf(IllegalArgumentException.class)
			.hasMessageContaining("수량은 음수가 될 수 없습니다.");
	}

}