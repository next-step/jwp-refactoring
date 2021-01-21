package kitchenpos.dto;

import kitchenpos.domain.Order;
import kitchenpos.domain.OrderStatus;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

public class OrderResponse {
	private long id;
	private long orderTableId;
	private OrderStatus orderStatus;
	private LocalDateTime orderedTime;
	private List<OrderLineItemResponse> orderLineItems;

	public OrderResponse() {
	}

	public static OrderResponse of(Order order) {
		List<OrderLineItemResponse> itemResponses = order.getOrderLineItems().stream()
				.map(OrderLineItemResponse::of)
				.collect(Collectors.toList());
		return new OrderResponse(order.getId(),
				order.getOrderTable().getId(),
				order.getOrderStatus(),
				order.getOrderedTime(),
				itemResponses);
	}

	public OrderResponse(long id, long orderTableId, OrderStatus orderStatus, LocalDateTime orderedTime, List<OrderLineItemResponse> orderLineItems) {
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
