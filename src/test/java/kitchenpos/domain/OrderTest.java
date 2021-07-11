package kitchenpos.domain;

import static org.assertj.core.api.Assertions.*;

import java.time.LocalDateTime;
import java.util.ArrayList;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

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

	@DisplayName("빈 테이블에서 주문시 오류 발생")
	@Test
	void testOrderEmptyTable() {
		OrderTable orderTable = new OrderTable(null, null, true);
		assertThatThrownBy(() -> {
			new Order(orderTable, OrderStatus.COMPLETION, LocalDateTime.of(2021, 7, 4, 0, 0), null);
		}).isInstanceOf(IllegalArgumentException.class)
			.hasMessageContaining("비어있는 테이블은 주문할 수 없습니다.");
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

}