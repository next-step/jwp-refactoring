package kitchenpos.order.domain;

import kitchenpos.common.Quantity;
import kitchenpos.menu.domain.Menu;

public interface OrderItem {

	Menu getMenu();
	Quantity getQuantity();

	static OrderItem of(Menu menu, Quantity quantity) {
		return new OrderItem() {
			@Override
			public Menu getMenu() {
				return menu;
			}

			@Override
			public Quantity getQuantity() {
				return quantity;
			}
		};
	}

	default OrderLineItem toOrderLineItem(Order order) {
		return new OrderLineItem(order, getMenu(), getQuantity());
	}
}
