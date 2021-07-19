package kitchenpos.order.domain;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class OrderTest {
	@Test
	@DisplayName("주문상태 변경 시 이미 완료 되어있으면 익셉션 발생생")
	void changeOrderStatusFailTest() {
		Order order = new Order(1L, 1L, OrderStatus.COMPLETION.name(), null);

		Assertions.assertThatThrownBy(() -> order.changeStatus(OrderStatus.MEAL.name()))
				.isInstanceOf(IllegalArgumentException.class);
	}
}
