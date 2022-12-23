package kitchenpos.order.domain;

import static kitchenpos.generator.OrderLineItemGenerator.*;
import static kitchenpos.generator.OrderTableGenerator.*;
import static org.assertj.core.api.Assertions.*;

import java.util.Collections;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

@DisplayName("주문 테스트")
class OrderTest {

	@Test
	@DisplayName("주문 생성")
	void createOrderTest() {
		assertThatNoException()
			.isThrownBy(() -> Order.of(
				비어있지_않은_5명_테이블().id(),
				OrderLineItems.fromSingle(주문_품목())));
	}

	@Test
	@DisplayName("주문 생성 - 주문 품목이 null이면 예외 발생")
	void createOrderWithNullOrderLineItemsTest() {
		assertThatIllegalArgumentException()
			.isThrownBy(() -> Order.of(
				비어있지_않은_5명_테이블().id(),
				null))
			.withMessage("주문 항목들은 필수입니다.");
	}

	@Test
	@DisplayName("주문 생성 - 주문 품목이 비어있으면 예외 발생")
	void createOrderWithEmptyOrderLineItemsTest() {
		assertThatIllegalArgumentException()
			.isThrownBy(() -> Order.of(
				비어있지_않은_5명_테이블().id(),
				OrderLineItems.from(Collections.emptyList())))
			.withMessage("주문 항목들이 비어있을 수 없습니다.");
	}

	@Test
	@DisplayName("주문 상태 변경")
	void changeOrderStatusTest() {
		Order order = Order.of(
			비어있지_않은_5명_테이블().id(),
			OrderLineItems.fromSingle(주문_품목()));

		order.updateStatus(OrderStatus.COMPLETION);

		Assertions.assertThat(order.orderStatus()).isEqualTo(OrderStatus.COMPLETION);
	}

	@Test
	@DisplayName("주문 상태 변경 - 이미 완료 된 주문은 변경 불가")
	void changeOrderStatusWithCompletionTest() {
		Order order = Order.of(
			비어있지_않은_5명_테이블().id(),
			OrderLineItems.fromSingle(주문_품목()));
		order.updateStatus(OrderStatus.COMPLETION);

		assertThatIllegalArgumentException()
			.isThrownBy(() -> order.updateStatus(OrderStatus.COOKING))
			.withMessage("이미 완료된 주문은 상태를 변경할 수 없습니다.");
	}
}
