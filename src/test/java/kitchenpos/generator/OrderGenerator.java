package kitchenpos.generator;

import static kitchenpos.generator.OrderLineItemGenerator.*;
import static kitchenpos.generator.OrderTableGenerator.*;
import static org.mockito.BDDMockito.*;

import java.util.Collections;

import kitchenpos.order.domain.Order;
import kitchenpos.order.domain.OrderLineItems;
import kitchenpos.order.domain.OrderStatus;

public class OrderGenerator {
	private OrderGenerator() {
	}

	public static Order 조리중_주문() {
		Order order = spy(Order.of(비어있지_않은_5명_테이블().id(), OrderLineItems.from(Collections.singletonList(주문_품목()))));
		given(order.getId()).willReturn(1L);
		return order;
	}

	public static Order 계산_완료_주문() {
		Order order = 조리중_주문();
		order.updateStatus(OrderStatus.COMPLETION);
		return order;
	}
}
