package kitchenpos.order.domain;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

import java.util.Collections;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import kitchenpos.order.exception.InvalidOrderException;

class OrderLineItemsTest {

	@DisplayName("주문항목 목록이 없거나 비어있으면 예외발생")
	@Test
	void of_invalid_order_line_items() {
		assertThatExceptionOfType(InvalidOrderException.class)
			.isThrownBy(() -> OrderLineItems.of(null));
		assertThatExceptionOfType(InvalidOrderException.class)
			.isThrownBy(() -> OrderLineItems.of(Collections.emptyList()));
	}
}
