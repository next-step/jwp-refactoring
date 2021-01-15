package kitchenpos.order.domain;

import static org.assertj.core.api.Assertions.assertThatIllegalArgumentException;

import java.util.ArrayList;
import java.util.Arrays;
import kitchenpos.ordertable.domain.OrderTable;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.test.util.ReflectionTestUtils;

class OrdersTest {

	@DisplayName("결제완료된 주문은 상태를 변경할 수 없음")
	@Test
	void changeOrderStatus() {
		Orders orders = new Orders();
		ReflectionTestUtils.setField(orders, "orderStatus", OrderStatus.COMPLETION.name());

		//when, then
		assertThatIllegalArgumentException()
			  .isThrownBy(() -> orders.changeStatus(OrderStatus.MEAL.name()))
			  .withMessage("결제 완료된 주문은 상태를 변경할 수 없습니다.")
		;
	}

	@DisplayName("비어있는 테이블은 주문을 등록할 수 없음")
	@Test
	void createWithEmptyTable() {
		//when, then
		assertThatIllegalArgumentException()
			  .isThrownBy(() -> new Orders(new OrderTable(true), OrderStatus.COOKING.name(),
					Arrays.asList(new OrderLineItem())))
			  .withMessage("테이블이 비어있습니다.")
		;
	}

	@DisplayName("주문항목이 비어있는 경우 주문을 등록할 수 없음")
	@Test
	void createWithEmptyOrderLineItems() {
		//when, then
		assertThatIllegalArgumentException()
			  .isThrownBy(() -> new Orders(new OrderTable(), OrderStatus.COOKING.name(),
					new ArrayList<>()))
			  .withMessage("주문 항목이 비어있습니다.")
		;
	}
}
