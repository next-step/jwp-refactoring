package kitchenpos.ordertable.domain;

import static org.assertj.core.api.Assertions.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import kitchenpos.ordertable.domain.domain.NumberOfGuests;
import kitchenpos.ordertable.exception.InvalidNumberOfGuestsException;

class NumberOfGuestsTest {

	@DisplayName("손님 수 생성")
	@Test
	void of() {
		assertThat(NumberOfGuests.of(0)).isEqualTo(NumberOfGuests.of(0));
	}

	@DisplayName("손님 수가 음수이면 예외발생")
	@Test
	void of_minus() {
		assertThatExceptionOfType(InvalidNumberOfGuestsException.class)
			.isThrownBy(() -> NumberOfGuests.of(-1));
	}
}
