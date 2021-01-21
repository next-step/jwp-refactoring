package kitchenpos.domain;

import kitchenpos.common.Quantity;

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
}
