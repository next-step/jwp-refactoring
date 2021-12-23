package kitchenpos.common;

import static org.assertj.core.api.Assertions.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class QuantityTest {

	@DisplayName("수량생성")
	@Test
	void of() {
		assertThat(Quantity.of(0L).getQuantity()).isEqualTo(0);
		assertThat(Quantity.of(100L).getQuantity()).isEqualTo(100);
	}

	@DisplayName("수량이 음수인 경우 예외발생")
	@Test
	void of_minus() {
		assertThatExceptionOfType(InvalidQuantityException.class)
			.isThrownBy(() -> Quantity.of(-1L));
	}
}
