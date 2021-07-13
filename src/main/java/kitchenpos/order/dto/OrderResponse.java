package kitchenpos.order.dto;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import kitchenpos.order.domain.Order;
import kitchenpos.table.dto.OrderTableResponse;

public class OrderResponse {

	private Long id;
	private OrderTableResponse orderTableResponse;
	private List<OrderLineItemResponse> orderLineItemResponses;
	private String orderStatus;
	private LocalDateTime orderedTime;

	public OrderResponse(Long id, OrderTableResponse orderTableResponse,
		List<OrderLineItemResponse> orderLineItemResponses,
		String orderStatus, LocalDateTime orderedTime) {
		this.id = id;
		this.orderTableResponse = orderTableResponse;
		this.orderLineItemResponses = orderLineItemResponses;
		this.orderStatus = orderStatus;
		this.orderedTime = orderedTime;
	}

	public Long getId() {
		return id;
	}

	public OrderTableResponse getOrderTableResponse() {
		return orderTableResponse;
	}

	public List<OrderLineItemResponse> getOrderLineItemResponses() {
		return orderLineItemResponses;
	}

	public String getOrderStatus() {
		return orderStatus;
	}

	public LocalDateTime getOrderedTime() {
		return orderedTime;
	}

	public static OrderResponse of(Order order) {
		return new OrderResponse(order.getId(), OrderTableResponse.of(order.getOrderTable()), OrderLineItemResponse.of(
			order.getOrderLineItems()), order.getOrderStatus().name(), order.getOrderedTime());
	}

	public static List<OrderResponse> of(List<Order> orders) {
		return orders.stream()
			.map(OrderResponse::of)
			.collect(Collectors.toList());
	}
}
