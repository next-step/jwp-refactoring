package kitchenpos.common;

import static org.assertj.core.api.Assertions.*;

import java.math.BigDecimal;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

class PriceTest {

	@DisplayName("가격이 null이면 예외발생")
	@Test
	void of_null_price() {
		assertThatExceptionOfType(InvalidPriceException.class)
			.isThrownBy(() -> Price.of(null));
	}

	@DisplayName("가격이 음수이면 예외발생")
	@ParameterizedTest
	@ValueSource(doubles = {-1, -0.1})
	void of_minus_price(double price) {
		assertThatExceptionOfType(InvalidPriceException.class)
			.isThrownBy(() -> Price.of(BigDecimal.valueOf(price)));
	}
}
