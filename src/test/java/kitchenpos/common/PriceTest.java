package kitchenpos.common;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

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

	@DisplayName("가격 덧셈")
	@Test
	void add() {
		final Price 천원 = Price.of(1_000);
		assertThat(천원.add(천원)).isEqualTo(Price.of(2_000));
	}

	@DisplayName("가격 곱셈")
	@Test
	void multiply() {
		final Price 오백원 = Price.of(500);
		assertThat(오백원.multiply(2)).isEqualTo(Price.of(1_000));
	}

	@DisplayName("가격 A가 가격 B보다 큰가")
	@Test
	void isBiggerThan() {
		final Price 백원 = Price.of(100);
		final Price 오십원 = Price.of(50);
		assertAll(() -> {
			assertThat(백원.isBiggerThan(백원)).isFalse();
			assertThat(백원.isBiggerThan(오십원)).isTrue();
			assertThat(오십원.isBiggerThan(백원)).isFalse();
		});
	}
}
