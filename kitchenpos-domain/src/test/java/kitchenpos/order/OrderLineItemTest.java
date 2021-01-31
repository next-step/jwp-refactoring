package kitchenpos.order;

import static org.assertj.core.api.Assertions.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class OrderLineItemTest {
	@DisplayName("메뉴가 없을 경울 IllegalArgumentException 발생")
	@Test
	void createNoMenu() {
		assertThatIllegalArgumentException()
			.isThrownBy(() -> OrderLineItem.builder().build());
	}
}
