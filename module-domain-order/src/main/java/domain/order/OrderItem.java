package domain.order;

import common.entity.Quantity;
import domain.menu.Menu;

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

	default OrderLineItem toOrderLineItem() {
		return new OrderLineItem(getMenu(), getQuantity());
	}
}
