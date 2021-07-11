package kitchenpos.order.dto;

import kitchenpos.menu.domain.Menu;
import kitchenpos.menu.domain.Quantity;
import kitchenpos.order.domain.OrderLineItem;

public class OrderLineItemRequest {

	private Long menuId;
	private Long orderId;
	private int quantity;

	public OrderLineItemRequest(Long menuId, Long orderId, int quantity) {
		this.menuId = menuId;
		this.orderId = orderId;
		this.quantity = quantity;
	}

	public Long getMenuId() {
		return menuId;
	}

	public Long getOrderId() {
		return orderId;
	}

	public int getQuantity() {
		return quantity;
	}

	public OrderLineItem toOrderLineItem(Menu menu) {
		return new OrderLineItem(menu, new Quantity(quantity));
	}

}

