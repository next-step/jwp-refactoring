package kitchenpos.order.domain;

import static org.assertj.core.api.Assertions.*;

import java.util.ArrayList;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class OrderLineItemsTest {
	@DisplayName("주문 항목이 비어있을 경우 IllegalArgumentException 발생")
	@Test
	void createWhenNoOrderLineItems() {
		assertThatIllegalArgumentException()
			.isThrownBy(() -> new OrderLineItems(1L, new ArrayList<>()));
	}
}
