package kitchenpos.domain;

import static java.time.LocalDateTime.*;
import static java.util.Arrays.*;
import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.time.LocalDateTime;
import java.util.Collections;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class OrderTest {

	@DisplayName("주문을 생성할 수 있다.")
	@Test
	void createTest() {
		OrderLineItem orderLineItem = new OrderLineItem(1L, 1L);
		OrderTable orderTable = mock(OrderTable.class);
		when(orderTable.getId()).thenReturn(1L);
		LocalDateTime orderedTime = now();

		Order order = Order.create(asList(orderLineItem), orderTable, orderedTime);

		assertThat(order.getOrderedTime()).isEqualTo(orderedTime);
		assertThat(orderLineItem.getOrder()).isEqualTo(order);
		assertThat(order.getOrderStatus()).isEqualTo(OrderStatus.COOKING);
	}

	@DisplayName("주문 항목이 없으면 주문을 생성할 수 있다.")
	@Test
	void createOrderWithoutOrderLineItems() {
		OrderTable orderTable = mock(OrderTable.class);
		when(orderTable.getId()).thenReturn(1L);
		LocalDateTime orderedTime = now();

		assertThatThrownBy(() ->  Order.create(Collections.emptyList(), orderTable, orderedTime))
			.isInstanceOf(IllegalArgumentException.class)
			.hasMessageContaining("주문 항목이 없습니다.");
	}

	@DisplayName("빈 주문테이블에서 주문할 수 없다.")
	@Test
	void createOrderWithEmptyOrderTableTest() {
		OrderLineItem orderLineItem = new OrderLineItem(1L, 1L);
		OrderTable orderTable = mock(OrderTable.class);
		when(orderTable.isEmpty()).thenReturn(true);

		assertThatThrownBy(() ->  Order.create(asList(orderLineItem), orderTable, now()))
			.isInstanceOf(IllegalArgumentException.class)
			.hasMessageContaining("빈테이블에서 주문할 수 없습니다.");
	}

	@DisplayName("계산료상태의 주문은 상태변경이 불가능하다.")
	@Test
	void changeCompletedOrderTest() {
		OrderLineItem orderLineItem = new OrderLineItem(1L, 1L);
		OrderTable orderTable = mock(OrderTable.class);
		Order order = Order.create(asList(orderLineItem), orderTable, now());
		order.complete();

		assertThatThrownBy(() -> order.startMeal())
			.isInstanceOf(IllegalArgumentException.class)
			.hasMessageContaining("계산 완료 주문은 상태를 변경할 수 없습니다.");
	}

	@DisplayName("주문의 상태를 변경 가능하다.")
	@Test
	void changeOrderTest() {
		OrderLineItem orderLineItem = new OrderLineItem(1L, 1L);
		OrderTable orderTable = mock(OrderTable.class);
		Order order = Order.create(asList(orderLineItem), orderTable, now());

		order.startMeal();
		assertThat(order.getOrderStatus()).isEqualTo(OrderStatus.MEAL);

		order.complete();
		assertThat(order.getOrderStatus()).isEqualTo(OrderStatus.COMPLETION);
	}
}