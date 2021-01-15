package kitchenpos.dto;

import kitchenpos.domain.Order;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

public class OrderResponse {
	private long id;
	private long orderTableId;
	private String orderStatus; // TODO: 2021-01-15 changetoEnum
	private LocalDateTime orderedTime;
	List<OrderLineItemResponse> orderLineItems;

	public OrderResponse() {
	}

	public static OrderResponse of(Order order) {
		List<OrderLineItemResponse> itemResponses = order.getOrderLineItems().stream()
				.map(OrderLineItemResponse::of)
				.collect(Collectors.toList());
		return new OrderResponse(order.getId(), order.getOrderTableId(), order.getOrderStatus(), order.getOrderedTime(),
				itemResponses);
	}

	public OrderResponse(long id, long orderTableId, String orderStatus, LocalDateTime orderedTime, List<OrderLineItemResponse> orderLineItems) {
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

	public String getOrderStatus() {
		return orderStatus;
	}

	public LocalDateTime getOrderedTime() {
		return orderedTime;
	}

	public List<OrderLineItemResponse> getOrderLineItems() {
		return orderLineItems;
	}
}
