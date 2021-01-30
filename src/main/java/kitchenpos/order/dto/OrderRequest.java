package kitchenpos.order.dto;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import kitchenpos.order.domain.OrderStatus;

public class OrderRequest {
	private Long orderTableId;
	private OrderStatus orderStatus;
	private List<OrderLineItemRequest> orderLineItems = new ArrayList<>();

	protected OrderRequest() {
	}

	public OrderRequest(Long orderTableId, List<OrderLineItemRequest> orderLineItems) {
		this.orderTableId = orderTableId;
		this.orderLineItems = orderLineItems;
	}

	public OrderRequest(OrderStatus orderStatus) {
		this.orderStatus = orderStatus;
	}

	public Long getOrderTableId() {
		return orderTableId;
	}

	public OrderStatus getOrderStatus() {
		return orderStatus;
	}

	public List<OrderLineItemRequest> getOrderLineItems() {
		return orderLineItems;
	}

	public List<Long> getMenuIds() {
		return orderLineItems.stream()
			.map(OrderLineItemRequest::getMenuId)
			.collect(Collectors.toList());
	}
}
