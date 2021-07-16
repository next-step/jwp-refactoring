package kitchenpos.order.dto;


import kitchenpos.order.domain.Order;
import kitchenpos.order.domain.OrderLineItem;
import kitchenpos.menu.domain.Menu;
import kitchenpos.menu.domain.Quantity;

public class OrderLineItemRequest {

	private Long menuId;
	private int quantity;

	public OrderLineItemRequest(Long menuId, int quantity) {
		this.menuId = menuId;
		this.quantity = quantity;
	}

	public Long getMenuId() {
		return menuId;
	}

	public int getQuantity() {
		return quantity;
	}

	public OrderLineItem toOrderLineItem(Menu menu, Order order) {
		return new OrderLineItem(menu, new Quantity(quantity));
	}

}

