package kitchenpos.order.domain;

import static org.assertj.core.api.Assertions.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class OrdersTest {

	@DisplayName("결제완료된 주문은 상태를 변경할 수 없음")
	@Test
	void changeOrderStatus() {
		// given // when
		Orders order = Orders.of(null, null, OrderStatus.COMPLETION);

		// then
		assertThatIllegalArgumentException()
			.isThrownBy(() -> order.changeOrderStatus(OrderStatus.MEAL))
			.withMessage("조리 상태가 완료일 경우 변경할 수 없습니다.");
	}
}
