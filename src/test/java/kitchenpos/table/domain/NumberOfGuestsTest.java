package kitchenpos.table.domain;

import static org.assertj.core.api.Assertions.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import kitchenpos.table.domain.NumberOfGuests;

class NumberOfGuestsTest {

	@DisplayName("방문 손님 수가 음수일 수 없다.")
	@Test
	void nonNegativeTest() {
		assertThatThrownBy(() -> NumberOfGuests.valueOf(-1))
			.isInstanceOf(IllegalArgumentException.class)
			.hasMessageContaining("방문 손님 수는 음수일 수 없습니다.");
	}


}