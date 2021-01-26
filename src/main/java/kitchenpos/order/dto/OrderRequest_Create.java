package kitchenpos.order.dto;

import domain.menu.Menu;
import domain.order.OrderItem;

import java.util.List;
import java.util.stream.Collectors;

public class OrderRequest_Create {
	private List<OrderLineItemRequest> orderLineItems;
	private long orderTableId;

	public OrderRequest_Create() {
	}

	public OrderRequest_Create(List<OrderLineItemRequest> orderLineItems, long orderTableId) {
		this.orderLineItems = orderLineItems;
		this.orderTableId = orderTableId;
	}

	public List<Long> getMenuIds() {
		return orderLineItems.stream()
				.map(OrderLineItemRequest::getMenuId)
				.collect(Collectors.toList());
	}

	public List<OrderItem> toOrderItems(List<Menu> menus) {
		return orderLineItems.stream()
				.map(iter -> iter.toOrderItem(menus))
				.collect(Collectors.toList());
	}

	public List<OrderLineItemRequest> getOrderLineItems() {
		return orderLineItems;
	}

	public long getOrderTableId() {
		return orderTableId;
	}
}
