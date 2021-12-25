package kitchenpos.common;

import static org.assertj.core.api.Assertions.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class QuantityTest {

	@DisplayName("수량생성")
	@Test
	void of() {
		assertThat(Quantity.of(0L)).isEqualTo(Quantity.of(0L));
	}

	@DisplayName("수량이 음수이면 예외발생")
	@Test
	void of_minus() {
		assertThatExceptionOfType(InvalidQuantityException.class)
			.isThrownBy(() -> Quantity.of(-1L));
	}
}
