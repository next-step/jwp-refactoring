package kitchenpos.order.domain;

import static org.assertj.core.api.Assertions.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import kitchenpos.order.order.domain.OrderStatus;

@DisplayName("주문 상태 테스트")
class OrderStatusTest {

	@Test
	@DisplayName("주문 상태가 완료인지 확인")
	void isCompletedTest() {
		assertThat(OrderStatus.COMPLETION.isCompleted()).isTrue();
	}

	@Test
	@DisplayName("주문 상태가 완료가 아닌지 확인")
	void isNotCompletedTest() {
		assertThat(OrderStatus.COOKING.isCompleted()).isFalse();
		assertThat(OrderStatus.MEAL.isCompleted()).isFalse();
	}
}
