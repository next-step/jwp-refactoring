package kitchenpos.order.domain;

import static org.assertj.core.api.Assertions.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import kitchenpos.table.domain.OrderTable;

class OrderTest {

	@DisplayName("이미 완료된 주문의 상태를 변경하려고 했을때 오류 발생")
	@Test
	void testOrderStatusChangeError() {
		OrderTable orderTable = new OrderTable(null, null, false);
		ArrayList<OrderLineItem> items = new ArrayList<>();
		items.add(new OrderLineItem(null, null, 3));
		Order order = new Order(orderTable, OrderStatus.COMPLETION, LocalDateTime.of(2021, 7, 4, 0, 0), items);
		assertThatThrownBy(() -> {
			order.changeState(OrderStatus.MEAL);
		}).isInstanceOf(IllegalArgumentException.class)
			.hasMessageContaining("이미 완료된 주문입니다.");
	}

	@DisplayName("주문항목이 없으면 오류 발생")
	@Test
	void testOrderEmptyOrderItems() {
		OrderTable orderTable = new OrderTable(null, null, false);
		assertThatThrownBy(() -> {
			new Order(orderTable, OrderStatus.COMPLETION, LocalDateTime.of(2021, 7, 4, 0, 0), null);
		}).isInstanceOf(IllegalArgumentException.class)
			.hasMessageContaining("주문 항목을 구성해야 주문이 가능합니다.");
	}

	@DisplayName("정상적인 주문 생성 테스트")
	@Test
	void testOrder() {
		List<OrderLineItem> orderLineItems = new ArrayList<>();
		OrderLineItem orderLineItem = new OrderLineItem(null, 1L, 1L);
		orderLineItems.add(orderLineItem);

		OrderTable orderTable = new OrderTable(null, null, false);
		LocalDateTime orderedDate = LocalDateTime.of(2021, 7, 4, 0, 0);
		Order actual = new Order(orderTable, OrderStatus.COOKING, orderedDate, orderLineItems);

		assertThat(actual.getOrderStatus()).isEqualTo(OrderStatus.COOKING);
		assertThat(actual.getOrderedTime()).isEqualTo(orderedDate);
		assertThat(actual.getOrderLineItems()).containsExactly(orderLineItem);
	}

}