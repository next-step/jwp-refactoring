package kitchenpos.generator;

import static org.mockito.BDDMockito.*;

import kitchenpos.order.domain.OrderLineItem;

public class OrderLineItemGenerator {

	private OrderLineItemGenerator() {
	}

	public static OrderLineItem 주문_품목() {
		OrderLineItem orderLineItem = spy(OrderLineItem.of(
			MenuGenerator.후라이드_세트(), 2));
		lenient().when(orderLineItem.seq()).thenReturn(1L);
		return orderLineItem;
	}

}
