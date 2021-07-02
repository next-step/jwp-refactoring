package kitchenpos.dto;

import java.util.List;
import java.util.stream.Collectors;

import kitchenpos.domain.OrderLineItem;
import kitchenpos.domain.OrderStatus;

public class OrderRequest {
	private long orderTableId;
	private OrderStatus orderStatus;
	private List<OrderLineItemRequest> orderLineItems;

	public long getOrderTableId() {
		return orderTableId;
	}

	public List<OrderLineItemRequest> getOrderLineItems() {
		return orderLineItems;
	}

	public List<Long> getMenuIds() {
		return orderLineItems.stream()
			.map(OrderLineItemRequest::getMenuId)
			.collect(Collectors.toList());
	}

	public int getOrderLineItemSize() {
		return orderLineItems.size();
	}

	public OrderStatus getOrderStatus() {
		return orderStatus;
	}

	public List<OrderLineItem> toOrderLineItems() {
		return orderLineItems.stream()
			.map(OrderLineItemRequest::toEntity)
			.collect(Collectors.toList());
	}
}
