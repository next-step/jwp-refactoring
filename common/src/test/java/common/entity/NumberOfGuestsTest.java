package common.entity;

import common.application.ValidationException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

class NumberOfGuestsTest {

	@DisplayName("정상적인 생성자 호출")
	@ParameterizedTest
	@ValueSource(ints = {0, 1, 99})
	void constructor(int numberOfGuests) {
		new NumberOfGuests(numberOfGuests);
	}

	@DisplayName("생성자 호출시 음수를 인자로 사용할 경우 예외 발생")
	@ParameterizedTest
	@ValueSource(ints =  {-1, -99})
	void constructor_Exception(int numberOfGuests) {
		assertThatThrownBy(() -> new NumberOfGuests(numberOfGuests))
				.isInstanceOf(ValidationException.class)
				.hasMessageMatching(NumberOfGuests.MSG_VALIDATE_GUESTS);
	}
}
