package kitchenpos.order.domain;

import static kitchenpos.menu.MenuFixture.*;
import static org.assertj.core.api.Assertions.*;
import static org.assertj.core.api.ThrowableAssert.*;

import java.util.Collections;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import kitchenpos.common.domain.Quantity;
import kitchenpos.ordertable.domain.NumberOfGuests;
import kitchenpos.ordertable.domain.OrderTable;

@DisplayName("주문")
class OrderTest {
	@DisplayName("생성")
	@Test
	void of() {
		// given
		OrderTable orderTable = OrderTable.of(NumberOfGuests.from(4), false);
		OrderLineItems orderLineItems = OrderLineItems.from(Collections.singletonList(OrderLineItem.of(
			후라이드후라이드_메뉴(),
			Quantity.from(1L))));

		// when
		Order order = Order.of(orderTable, orderLineItems);

		// then
		assertThat(order).isNotNull();
	}

	@DisplayName("생성 실패 - 주문 테이블이 비어있는 경우")
	@Test
	void ofFailOnEmptyOrderTable() {
		// given
		OrderTable orderTable = OrderTable.of(NumberOfGuests.from(4), true);
		OrderLineItems orderLineItems = OrderLineItems.from(Collections.singletonList(OrderLineItem.of(
			후라이드후라이드_메뉴(),
			Quantity.from(1L))));

		// when
		ThrowingCallable throwingCallable = () -> Order.of(orderTable, orderLineItems);

		// then
		assertThatThrownBy(throwingCallable).isInstanceOf(IllegalArgumentException.class);
	}
}
