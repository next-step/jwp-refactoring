package api.order.dto;

import common.entity.Quantity;
import domain.menu.Menu;
import domain.order.OrderItem;

import java.util.List;
import java.util.Objects;

public class OrderLineItemRequest {
	private long menuId;
	private long quantity;

	public OrderLineItemRequest() {
	}

	public OrderLineItemRequest(long menuId, long quantity) {
		this.menuId = menuId;
		this.quantity = quantity;
	}

	public long getMenuId() {
		return menuId;
	}

	public long getQuantity() {
		return quantity;
	}

	public OrderItem toOrderItem(List<Menu> menus) {
		Menu menu = menus.stream()
				.filter(iter -> Objects.equals(menuId, iter.getId()))
				.findFirst()
				.orElseThrow(() -> new IllegalArgumentException("cannot find menu"));
		return OrderItem.of(menu, new Quantity(quantity));
	}
}
