package kitchenpos.order.domain;

import static java.time.LocalDateTime.*;
import static java.util.Arrays.*;
import static kitchenpos.order.domain.OrderMenuTest.*;
import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import kitchenpos.product.domain.Name;
import kitchenpos.product.domain.Price;
import kitchenpos.table.domain.OrderTable;

class OrderTest {

	@DisplayName("주문의 테이블 id, 주문 항목, 주문 시각은 필수정보다")
	@Test
	void createWithNullTest() {
		OrderMenu orderMenu = OrderMenu.of(1L, Name.valueOf("치킨"), Price.wonOf(16000));
		OrderLineItem orderLineItem = new OrderLineItem(orderMenu, 1);
		OrderLineItems orderLineItems = OrderLineItems.of(asList(orderLineItem));

		assertAll(
			() -> assertThatThrownBy(() -> new Order(null, orderLineItems, now()))
				.isInstanceOf(IllegalArgumentException.class)
				.hasMessageContaining("주문 필수 정보가 없습니다."),
			() -> assertThatThrownBy(() -> new Order(new OrderTableId(1L), null, now()))
				.isInstanceOf(IllegalArgumentException.class)
				.hasMessageContaining("주문 필수 정보가 없습니다."),
			() -> assertThatThrownBy(() -> new Order(new OrderTableId(1L), orderLineItems, null))
				.isInstanceOf(IllegalArgumentException.class)
				.hasMessageContaining("주문 필수 정보가 없습니다.")
		);
	}

	@DisplayName("계산완료상태의 주문은 상태변경이 불가능하다.")
	@Test
	void changeCompletedOrderTest() {
		// given
		OrderLineItem orderLineItem = new OrderLineItem(ORDER_MENU, 1);
		OrderTable orderTable = new OrderTable(1, false);
		Order completedOrder = orderTable.createOrder(OrderLineItems.of(orderLineItem), now());
		completedOrder.complete();

		// when
		// than
		assertThatThrownBy(() -> completedOrder.startMeal())
			.isInstanceOf(IllegalArgumentException.class)
			.hasMessageContaining("계산 완료 주문은 상태를 변경할 수 없습니다.");
	}

	@DisplayName("주문의 상태를 변경 가능하다.")
	@Test
	void changeOrderTest() {
		// given
		OrderLineItem orderLineItem = new OrderLineItem(ORDER_MENU, 1);
		OrderTable orderTable = new OrderTable(1, false);
		Order order = orderTable.createOrder(OrderLineItems.of(orderLineItem), now());

		// when
		order.changeStatus(OrderStatus.MEAL);

		// then
		assertThat(order.getOrderStatus()).isEqualTo(OrderStatus.MEAL);

		// when
		order.changeStatus(OrderStatus.COMPLETION);

		// then
		assertThat(order.getOrderStatus()).isEqualTo(OrderStatus.COMPLETION);
	}
}
