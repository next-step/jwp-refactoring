package kitchenpos.dto;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import kitchenpos.domain.Order;
import kitchenpos.domain.OrderLineItem;

public class OrderRequest {
	private Long orderTableId;
	private String orderStatus;
	private List<OrderLineItemRequest> orderLineItems = new ArrayList<>();

	public OrderRequest() {
	}

	public OrderRequest(String orderStatus) {
		this.orderStatus = orderStatus;
	}

	public OrderRequest(Long orderTableId, String orderStatus,
		List<OrderLineItemRequest> orderLineItems) {
		this.orderTableId = orderTableId;
		this.orderStatus = orderStatus;
		if (Objects.nonNull(orderLineItems)) {
			this.orderLineItems = orderLineItems;
		}
	}

	public Long getOrderTableId() {
		return orderTableId;
	}

	public String getOrderStatus() {
		return orderStatus;
	}

	public List<OrderLineItemRequest> getOrderLineItems() {
		return orderLineItems;
	}

	public List<OrderLineItem> toOrderLineItems() {
		return orderLineItems.stream()
			.map(OrderLineItemRequest::toOrderLineItem)
			.collect(Collectors.toList());
	}

	public Order toOder() {
		return new Order(this.orderTableId, this.orderStatus);
	}
}
