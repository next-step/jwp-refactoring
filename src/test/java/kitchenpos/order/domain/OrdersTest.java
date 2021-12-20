package kitchenpos.order.domain;

import static org.assertj.core.api.Assertions.*;

import java.util.Arrays;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import kitchenpos.common.ErrorCode;
import kitchenpos.order.exception.OrderException;

@DisplayName("주문 일급 컬렉션 : 단위 테스트")
class OrdersTest {

	private OrderTable orderTable;
	private Orders orders;

	@BeforeEach
	void setup() {
		orderTable = OrderTable.of(100, false);
	}

	@DisplayName("완료되지 않은 주문이 있을 때 예외처리 테스트")
	@Test
	void hasNotCompletionOrder() {
		// given
		Order order1 = Order.of(orderTable, OrderStatus.COMPLETION);
		Order order2 = Order.of(orderTable, OrderStatus.COOKING);
		orders = Orders.from(Arrays.asList(order1, order2));

		// when // then
		assertThatThrownBy(() -> {
			orders.hasNotCompletionOrder();
		}).isInstanceOf(OrderException.class)
			.hasMessageContaining(ErrorCode.ORDER_IS_NOT_COMPLETION.getMessage());
	}
}
