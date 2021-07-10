package kitchenpos.order.domain;

import static java.time.LocalDateTime.*;
import static kitchenpos.TextFixture.*;
import static org.assertj.core.api.Assertions.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import kitchenpos.table.domain.OrderTable;

class OrderTest {

	@DisplayName("계산완료상태의 주문은 상태변경이 불가능하다.")
	@Test
	void changeCompletedOrderTest() {
		// given
		OrderTable orderTable = new OrderTable(1, false);
		Order completedOrder = orderTable.createOrder(주문항목들_후라이드_1개_양념_1개, now());
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
		OrderTable orderTable = new OrderTable(1, false);
		Order order = orderTable.createOrder(주문항목들_후라이드_1개_양념_1개, now());

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