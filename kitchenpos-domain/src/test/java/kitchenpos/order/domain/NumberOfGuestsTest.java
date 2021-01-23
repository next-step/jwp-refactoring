package kitchenpos.order.domain;

import static org.assertj.core.api.Assertions.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class NumberOfGuestsTest {

	@Test
	@DisplayName("손님 수를 음수로 생성하려고 하면 IllegalArgumentException 을 throw 해야한다.")
	void negativePrice() {
		//when-then
		assertThatThrownBy(() -> new NumberOfGuests(-200))
			.isInstanceOf(IllegalArgumentException.class);
	}
}
