package kitchenpos.orders.dto;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import kitchenpos.orders.domain.Order;

public class OrderResponse {

	private Long id;
	private Long orderTableId;
	private String orderStatus;
	private LocalDateTime orderedTime;
	private List<OrderLineItemResponse> orderLineItemResponses;

	public OrderResponse() {
	}

	public OrderResponse(long id, long orderTableId, String orderStatus, LocalDateTime orderedTime,
		List<OrderLineItemResponse> orderLineItemResponses) {
		this.id = id;
		this.orderTableId = orderTableId;
		this.orderStatus = orderStatus;
		this.orderedTime = orderedTime;
		this.orderLineItemResponses = orderLineItemResponses;
	}

	public static OrderResponse of(Order order) {
		List<OrderLineItemResponse> orderLineItemResponses = order.getOrderLineItems()
			.stream()
			.map(OrderLineItemResponse::of)
			.collect(Collectors.toList());

		return new OrderResponse(order.getId(), order.getOrderTableId(), order.getOrderStatus().name(), order.getOrderedTime(),
			orderLineItemResponses);
	}

	public Long getId() {
		return id;
	}

	public Long getOrderTableId() {
		return orderTableId;
	}

	public String getOrderStatus() {
		return orderStatus;
	}

	public LocalDateTime getOrderedTime() {
		return orderedTime;
	}

	public List<OrderLineItemResponse> getOrderLineItemResponses() {
		return orderLineItemResponses;
	}
}
