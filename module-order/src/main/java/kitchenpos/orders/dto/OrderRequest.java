package kitchenpos.orders.dto;

import java.util.List;
import java.util.stream.Collectors;


import kitchenpos.common.domain.Quantity;
import kitchenpos.orders.domain.Order;
import kitchenpos.orders.domain.OrderLineItem;
import kitchenpos.orders.domain.OrderLineItems;
import kitchenpos.orders.domain.OrderStatus;

public class OrderRequest {
	private Long orderTableId;
	private String orderStatus;
	private List<OrderLineItemRequest> orderLineItemRequests;

	public OrderRequest() {
	}

	public OrderRequest(String orderStatus) {
		this.orderStatus = orderStatus;
	}

	public OrderRequest(long orderTableId, List<OrderLineItemRequest> orderLineItemRequests) {
		this.orderTableId = orderTableId;
		this.orderLineItemRequests = orderLineItemRequests;
	}

	public Long getOrderTableId() {
		return orderTableId;
	}

	public String getOrderStatus() {
		return orderStatus;
	}

	public List<OrderLineItemRequest> getOrderLineItemRequests() {
		return orderLineItemRequests;
	}

	public Order toOrder() {
		return new Order(orderTableId, OrderStatus.COOKING);
	}

	public OrderLineItems toOrderLineItems() {
		return new OrderLineItems(orderLineItemRequests
			.stream()
			.map(ol -> new OrderLineItem(ol.getMenuId(), Quantity.valueOf(ol.getQuantity())))
			.collect(Collectors.toList()));
	}
}

