package kitchenpos.order.dto;

import kitchenpos.menu.domain.Menu;
import kitchenpos.order.domain.OrderLineItem;
import kitchenpos.order.domain.OrderMenu;

public class OrderLineItemRequest {
	private long menuId;

	private long quantity;

	public long getMenuId() {
		return menuId;
	}

	public long getQuantity() {
		return quantity;
	}

	boolean isEqualMenuId(long menuId) {
		return this.menuId == menuId;
	}

	OrderLineItem toEntity(Menu menu) {
		return new OrderLineItem(OrderMenu.of(menu.getId(), menu.getName(), menu.getPrice()), quantity);
	}
}
