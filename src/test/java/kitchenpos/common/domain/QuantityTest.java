package kitchenpos.common.domain;

import static org.assertj.core.api.Assertions.*;

import org.assertj.core.api.ThrowableAssert.ThrowingCallable;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullSource;
import org.junit.jupiter.params.provider.ValueSource;

@DisplayName("수량")
class QuantityTest {

	@DisplayName("생성")
	@Test
	void from() {
		// given
		long quantity = 2;

		// when
		Quantity menuProductQuantity = Quantity.from(quantity);

		// then
		assertThat(menuProductQuantity.getValue()).isEqualTo(quantity);
	}

	@DisplayName("생성 실패 - 수량이 없거나 음수인 경우")
	@ParameterizedTest
	@NullSource
	@ValueSource(strings = {"-1"})
	void fromFailOnNullOrNegativeQuantity(Long quantity) {
		// given

		// when
		ThrowingCallable throwingCallable = () -> Quantity.from(quantity);

		// then
		assertThatThrownBy(throwingCallable).isInstanceOf(IllegalArgumentException.class);
	}

	@DisplayName("동등성 비교")
	@Test
	void equals() {
		// given
		long quantity = 2;

		// when
		Quantity actual = Quantity.from(quantity);
		Quantity expected = Quantity.from(quantity);

		// then
		assertThat(actual).isEqualTo(expected);
	}
}
