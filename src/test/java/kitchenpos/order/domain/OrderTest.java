package kitchenpos.order.domain;

import static org.assertj.core.api.Assertions.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import kitchenpos.common.ErrorCode;
import kitchenpos.order.exception.OrderException;

@DisplayName("주문 : 단위 테스트")
class OrderTest {

	private OrderTable orderTable;

	@DisplayName("주문 생성시 주문 테이블을 비어있지 않은 상태로 바꾼.")
	@Test
	void createOrderEmptyTable() {
		// given
		orderTable = OrderTable.of(10, true);

		// when
		Order order = Order.of(orderTable, OrderStatus.COOKING);

		// then
		assertThat(order.getOrderTable().isEmpty()).isFalse();
	}

	@DisplayName("주문이 완료 상태인 경우 상태 변경 요청시 예외처리 테스트")
	@Test
	void changeOrderStatusIsCompletionOrder() {
		// given
		orderTable = OrderTable.of(10, false);
		Order order = Order.of(orderTable, OrderStatus.COMPLETION);

		// when // then
		assertThatThrownBy(() -> {
			order.updateOrderStatus(OrderStatus.COOKING);
		}).isInstanceOf(OrderException.class)
			.hasMessageContaining(ErrorCode.ORDER_IS_COMPLETION.getMessage());
	}
}
