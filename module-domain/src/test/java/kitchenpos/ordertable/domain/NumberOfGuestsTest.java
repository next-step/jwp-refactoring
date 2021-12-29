package kitchenpos.ordertable.domain;

import static org.assertj.core.api.Assertions.*;

import org.assertj.core.api.ThrowableAssert.ThrowingCallable;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullSource;
import org.junit.jupiter.params.provider.ValueSource;

@DisplayName("손님 수")
class NumberOfGuestsTest {

	@DisplayName("생성")
	@ParameterizedTest
	@ValueSource(strings = {"0", "4"})
	void from(int value) {
		// given

		// when
		NumberOfGuests numberOfGuests = NumberOfGuests.from(value);

		// then
		assertThat(numberOfGuests.getValue()).isEqualTo(value);
	}

	@DisplayName("생성 실패 - 손님 수가 없거나 음수인 경우")
	@ParameterizedTest
	@NullSource
	@ValueSource(strings = {"-1"})
	void fromFailOnNullOrNegativeNumberOfGuests(Integer value) {
		// given

		// when
		ThrowingCallable throwingCallable = () -> NumberOfGuests.from(value);

		// then
		assertThatThrownBy(throwingCallable).isInstanceOf(IllegalArgumentException.class);
	}

	@DisplayName("동등성 비교")
	@Test
	void equals() {
		// given
		int value = 4;

		// when
		NumberOfGuests aNumberOfGuests = NumberOfGuests.from(value);
		NumberOfGuests bNumberOfGuests = NumberOfGuests.from(value);

		// then
		assertThat(aNumberOfGuests).isEqualTo(bNumberOfGuests);
	}
}
