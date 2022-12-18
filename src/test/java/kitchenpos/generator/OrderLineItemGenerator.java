package kitchenpos.generator;

import kitchenpos.order.domain.OrderLineItem;

public class OrderLineItemGenerator {

	private OrderLineItemGenerator() {
	}

	public static OrderLineItem 주문_품목() {
		return new OrderLineItem(1L, 1L, 1L, 2);
	}

	public static OrderLineItem 주문_품목(Long menuId, int quantity) {
		return new OrderLineItem(null, null, menuId, quantity);
	}
}
