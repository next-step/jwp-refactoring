package kitchenpos.order.dto.order;

import kitchenpos.menu.domain.menu.Menu;
import kitchenpos.order.domain.order.OrderLineItem;

public class OrderLineItemRequest {

	private Long id;
	private Long menuId;
	private long quantity;

	protected OrderLineItemRequest() {
	}

	public OrderLineItemRequest(final Long menuId, final long quantity) {
		this.menuId = menuId;
		this.quantity = quantity;
	}

	public Long getId() {
		return id;
	}

	public void setId(final Long id) {
		this.id = id;
	}

	public Long getMenuId() {
		return menuId;
	}

	public void setMenuId(final Long menuId) {
		this.menuId = menuId;
	}

	public long getQuantity() {
		return quantity;
	}

	public void setQuantity(final long quantity) {
		this.quantity = quantity;
	}

	public OrderLineItem toOrderLineItem(final Menu menu) {
		return new OrderLineItem(menu, this.quantity);
	}
}
