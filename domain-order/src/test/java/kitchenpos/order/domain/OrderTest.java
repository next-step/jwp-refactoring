package kitchenpos.order.domain;

import static org.assertj.core.api.Assertions.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import kitchenpos.common.exception.ErrorCode;
import kitchenpos.order.exception.OrderException;

@DisplayName("주문 : 단위 테스트")
class OrderTest {

	@DisplayName("주문이 완료 상태인 경우 상태 변경 요청시 예외처리 테스트")
	@Test
	void changeOrderStatusIsCompletionOrder() {
		// given
		Order order = Order.of(1L, OrderStatus.COMPLETION);

		// when // then
		assertThatThrownBy(() -> {
			order.updateOrderStatus(OrderStatus.COOKING);
		}).isInstanceOf(OrderException.class)
			.hasMessageContaining(ErrorCode.ORDER_IS_COMPLETION.getMessage());
	}
}
