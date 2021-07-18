package kitchenpos.table.domain;

import static org.assertj.core.api.Assertions.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class NumberOfGuestsTest {
	@DisplayName("인원 수 객체를 만든다.")
	@Test
	void createNumberOfGuestsInHappyCase() {
		NumberOfGuests numberOfGuests = new NumberOfGuests(5);

		assertThat(numberOfGuests).isEqualTo(new NumberOfGuests(5));
	}

	@DisplayName("인원 수는 0보다 작을 수 없다.")
	@Test
	void createNumberOfGuestsWithMinus() {
		assertThatThrownBy(() -> new NumberOfGuests(-5)).isInstanceOf(IllegalArgumentException.class);
	}
}
