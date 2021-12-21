package kitchenpos.common.domain;

import static org.assertj.core.api.Assertions.*;
import static org.assertj.core.api.ThrowableAssert.*;

import java.math.BigDecimal;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullSource;
import org.junit.jupiter.params.provider.ValueSource;

import kitchenpos.common.domain.Price;

@DisplayName("가격")
class PriceTest {
	@DisplayName("생성")
	@ParameterizedTest
	@ValueSource(strings = {"0", "17000"})
	void of(BigDecimal value) {
		// given

		// when
		Price price = Price.of(value);

		// then
		assertThat(price.getValue()).isEqualTo(value);
	}

	@DisplayName("생성 실패 - 가격이 없거나 음수인 경우")
	@ParameterizedTest
	@NullSource
	@ValueSource(strings = {"-10000"})
	void ofFailOnNullOrNegativePrice(BigDecimal value) {
		// given

		// when
		ThrowingCallable throwingCallable = () -> Price.of(value);

		// then
		assertThatThrownBy(throwingCallable).isInstanceOf(IllegalArgumentException.class);
	}

	@DisplayName("동등성 비교")
	@Test
	void equals() {
		// given
		BigDecimal value = BigDecimal.valueOf(10000);

		// when
		Price aPrice = Price.of(value);
		Price bPrice = Price.of(value);

		// then
		assertThat(aPrice).isEqualTo(bPrice);
	}
}
