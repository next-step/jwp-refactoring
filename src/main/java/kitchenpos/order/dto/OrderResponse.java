package kitchenpos.order.dto;


import kitchenpos.order.domain.Order;
import kitchenpos.order.domain.OrderLineItem;
import kitchenpos.order.domain.OrderStatus;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class OrderResponse {
	private long id;
	private long orderTableId;
	private OrderStatus orderStatus;
	private LocalDateTime orderedTime;
	private List<OrderLineItemResponse> orderLineItems;

	OrderResponse() {
	}

	public static OrderResponse of(Order order) {
		return new OrderResponse(order.getId(),
				order.getOrderTableId(),
				order.getOrderStatus(),
				order.getOrderedTime(),
				getOrderLineItemResponses(order));
	}

	private static List<OrderLineItemResponse> getOrderLineItemResponses(Order order) {
		List<OrderLineItemResponse> itemResponses = new ArrayList<>();
		for (OrderLineItem orderLineItem : order.getOrderLineItems()) {
			itemResponses.add(OrderLineItemResponse.of(orderLineItem));
		}
		return itemResponses;
	}

	private OrderResponse(long id, long orderTableId, OrderStatus orderStatus, LocalDateTime orderedTime, List<OrderLineItemResponse> orderLineItems) {
		this.id = id;
		this.orderTableId = orderTableId;
		this.orderStatus = orderStatus;
		this.orderedTime = orderedTime;
		this.orderLineItems = orderLineItems;
	}

	public long getId() {
		return id;
	}

	public long getOrderTableId() {
		return orderTableId;
	}

	public OrderStatus getOrderStatus() {
		return orderStatus;
	}

	public LocalDateTime getOrderedTime() {
		return orderedTime;
	}

	public List<OrderLineItemResponse> getOrderLineItems() {
		return orderLineItems;
	}
}
