package kitchenpos.common.domain;

import static org.assertj.core.api.Assertions.*;
import static org.assertj.core.api.ThrowableAssert.*;

import java.math.BigDecimal;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullSource;
import org.junit.jupiter.params.provider.ValueSource;

@DisplayName("가격")
class PriceTest {
	@DisplayName("생성")
	@ParameterizedTest
	@ValueSource(strings = {"0", "17000"})
	void from(BigDecimal value) {
		// given

		// when
		Price price = Price.from(value);

		// then
		assertThat(price.getValue()).isEqualTo(value);
	}

	@DisplayName("생성 실패 - 가격이 없거나 음수인 경우")
	@ParameterizedTest
	@NullSource
	@ValueSource(strings = {"-10000"})
	void fromFailOnNullOrNegativePrice(BigDecimal value) {
		// given

		// when
		ThrowingCallable throwingCallable = () -> Price.from(value);

		// then
		assertThatThrownBy(throwingCallable).isInstanceOf(IllegalArgumentException.class);
	}

	@DisplayName("동등성 비교")
	@Test
	void equals() {
		// given
		BigDecimal value = BigDecimal.valueOf(10000);

		// when
		Price aPrice = Price.from(value);
		Price bPrice = Price.from(value);

		// then
		assertThat(aPrice).isEqualTo(bPrice);
	}

	@DisplayName("더하기")
	@Test
	void add() {
		// given
		Price augend = Price.from(BigDecimal.valueOf(10000));
		Price addend = Price.from(BigDecimal.valueOf(20000));

		// when
		Price sum = augend.add(addend);

		// then
		assertThat(sum).isEqualTo(Price.from(BigDecimal.valueOf(30000)));
	}

	@DisplayName("곱하기")
	@Test
	void multiply() {
		// given
		Price multiplicand = Price.from(BigDecimal.valueOf(10000));
		Quantity multiplier = Quantity.from(2L);

		// when
		Price product = multiplicand.multiply(multiplier);

		// then
		assertThat(product).isEqualTo(Price.from(BigDecimal.valueOf(20000)));
	}
}
