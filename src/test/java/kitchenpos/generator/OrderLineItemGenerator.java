package kitchenpos.generator;

import static org.mockito.BDDMockito.*;

import kitchenpos.order.domain.OrderLineItem;

public class OrderLineItemGenerator {

	private OrderLineItemGenerator() {
	}

	public static OrderLineItem 주문_품목() {
		OrderLineItem orderLineItem = spy(OrderLineItem.of(1L, 2));
		lenient().when(orderLineItem.getSeq()).thenReturn(1L);
		return orderLineItem;
	}

	public static OrderLineItem 주문_품목(Long menuId, int quantity) {
		return OrderLineItem.of(menuId, quantity);
	}
}
