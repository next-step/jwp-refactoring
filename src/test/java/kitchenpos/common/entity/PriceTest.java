package kitchenpos.common.entity;

import kitchenpos.common.application.ValidationException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.ValueSource;

import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class PriceTest {

	@DisplayName("생성자에 null 을 사용할 경우 예외발생.")
	@Test
	void constructor_ExceptionNull() {
		assertThatThrownBy(() -> new Price(null))
				.isInstanceOf(ValidationException.class)
				.hasMessageMatching(Price.MSG_PRICE_NOT_NULL);
	}

	@DisplayName("생성자에 음수를 사용할 경우 예외발생.")
	@ParameterizedTest
	@ValueSource(longs = {-1, -1000})
	void constructor_ExceptionNegative(long value) {
		assertThatThrownBy(() -> new Price(new BigDecimal(value)))
				.isInstanceOf(ValidationException.class)
				.hasMessageMatching(Price.MSG_PRICE_MUST_EQUAL_OR_GREATER_THAN_ZERO);
	}

	@DisplayName("올바른 인자를 사용하여 생성자 사용")
	@ParameterizedTest
	@ValueSource(longs = {1, 1000})
	void constructor(long value) {
		new Price(new BigDecimal(value));
	}

	@DisplayName("더하기 연산")
	@ParameterizedTest
	@CsvSource(value = {"1000,5,1005", "2000,5000,7000"})
	void add(long value1, long value2, long result) {
		final Price price1 = new Price(new BigDecimal(value1));
		final Price price2 = new Price(new BigDecimal(value2));

		final Price expected = new Price(new BigDecimal(result));
		assertThat(price1.add(price2)).isEqualTo(expected);
	}

	@DisplayName("곱셈 연산")
	@ParameterizedTest
	@CsvSource(value = {"1000,5,5000", "350,2,700"})
	void multiply(long value1, long value2, long result) {
		final Price price = new Price(new BigDecimal(value1));
		final Quantity quantity = new Quantity(value2);

		final Price expected = new Price(new BigDecimal(result));
		assertThat(price.multiply(quantity)).isEqualTo(expected);
	}
}
