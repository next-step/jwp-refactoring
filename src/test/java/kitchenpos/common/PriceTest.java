package kitchenpos.common;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

class PriceTest {

	@DisplayName("생성자에 null 을 사용할 경우 예외발생.")
	@Test
	void constructor_ExceptionNull() {
		assertThatThrownBy(() -> new Price(null))
				.isInstanceOf(IllegalArgumentException.class);
	}

	@DisplayName("생성자에 음수를 사용할 경우 예외발생.")
	@ParameterizedTest
	@ValueSource(longs = {-1, -1000})
	void constructor_ExceptionNegative(long value) {
		assertThatThrownBy(() -> new Price(new BigDecimal(value)))
				.isInstanceOf(IllegalArgumentException.class);
	}

	@DisplayName("올바른 인자를 사용하여 생성자 사용")
	@ParameterizedTest
	@ValueSource(longs = {1, 1000})
	void constructor(long value) {
		new Price(new BigDecimal(value));
	}
}
