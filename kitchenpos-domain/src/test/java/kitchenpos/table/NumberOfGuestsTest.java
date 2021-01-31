package kitchenpos.table;

import static org.assertj.core.api.Assertions.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class NumberOfGuestsTest {
	@DisplayName("방문한 손님 수가 0보다 작으면 IllegalArgumentException 발생")
	@Test
	void lessThanZero() {
		assertThatIllegalArgumentException()
			.isThrownBy(() -> new NumberOfGuests(-1));
	}
}
