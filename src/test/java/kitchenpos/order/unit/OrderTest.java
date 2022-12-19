package kitchenpos.order.unit;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

import java.util.Arrays;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.test.util.ReflectionTestUtils;

import kitchenpos.common.Name;
import kitchenpos.common.Price;
import kitchenpos.exception.ErrorMessage;
import kitchenpos.order.domain.Order;
import kitchenpos.order.domain.OrderLineItem;
import kitchenpos.order.domain.OrderLineItems;
import kitchenpos.order.domain.OrderMenu;
import kitchenpos.order.domain.OrderStatus;
import kitchenpos.table.domain.OrderTable;

@DisplayName("주문 관련 단위테스트")
public class OrderTest {
	private OrderLineItems 주문상품;
	private OrderTable 채워진_테이블;
	private OrderTable 빈_테이블;

	@BeforeEach
	void setUp() {
		OrderMenu 주문메뉴 = OrderMenu.of(1L, Name.of("짜장세트"), Price.of(6000));
		주문상품 = OrderLineItems.of(Arrays.asList(OrderLineItem.of(주문메뉴, 1)));
		빈_테이블 = OrderTable.of(2, true);
		채워진_테이블 = OrderTable.of(2, false);
		ReflectionTestUtils.setField(빈_테이블, "id", 1L);
		ReflectionTestUtils.setField(채워진_테이블, "id", 2L);

	}

	@DisplayName("주문을 생성할 수 있다.")
	@Test
	void createOrder() {
		// when
		Order 주문 = Order.of(채워진_테이블.getId(), 주문상품);
		// then
		assertAll(
			() -> assertThat(주문.getOrderTableId()).isEqualTo(채워진_테이블.getId()),
			() -> assertThat(주문.getOrderStatus()).isEqualTo(OrderStatus.COOKING),
			() -> assertThat(주문.getOrderLineItems().getSize()).isEqualTo(1)
		);
	}

	@DisplayName("주문의 상태를 변경할 수 있다.")
	@Test
	void updateStatus() {
		// given
		Order 주문 = Order.of(채워진_테이블.getId(), 주문상품);
		// when
		주문.updateOrderStatus(OrderStatus.MEAL);
		// then
		assertThat(주문.getOrderStatus()).isEqualTo(OrderStatus.MEAL);
	}

	@DisplayName("주문의이 완료된경우 주문 상태를 변경하면 예외가 발생한다.")
	@Test
	void updateStatus_when_completed_exception() {
		// given
		Order 주문 = Order.of(채워진_테이블.getId(), 주문상품);
		주문.updateOrderStatus(OrderStatus.COMPLETION);
		// when - then
		assertThatThrownBy(() -> 주문.updateOrderStatus(OrderStatus.MEAL))
			.isInstanceOf(IllegalArgumentException.class)
			.hasMessage(ErrorMessage.CANNOT_CHANGE_ORDER_STATUS_WHEN_COMPLETED);
	}

}
