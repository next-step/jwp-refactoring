package kitchenpos.order.generator.generator;

import static kitchenpos.generator.OrderLineItemMenuGenerator.*;
import static org.mockito.BDDMockito.*;

import kitchenpos.order.order.domain.OrderLineItem;

public class OrderLineItemGenerator {

	private OrderLineItemGenerator() {
	}

	public static OrderLineItem 주문_품목() {
		OrderLineItem orderLineItem = spy(OrderLineItem.of(
			주문_품목_메뉴(), 2));
		lenient().when(orderLineItem.seq()).thenReturn(1L);
		return orderLineItem;
	}

}
