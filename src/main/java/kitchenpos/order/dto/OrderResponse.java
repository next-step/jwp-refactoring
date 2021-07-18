package kitchenpos.order.dto;

import kitchenpos.order.domain.Order;
import kitchenpos.order.domain.OrderStatus;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class OrderResponse {
	private Long orderId;
	private Long orderTableId;
	private OrderStatus orderStatus;
	private LocalDateTime orderedTime;
	private List<OrderLineItemResponse> orderLineItemResponses;

	public OrderResponse() {
	}

	public OrderResponse(Long orderId, Long orderTableId, OrderStatus orderStatus, LocalDateTime orderedTime, List<OrderLineItemResponse> orderLineItemResponses) {
		this.orderId = orderId;
		this.orderTableId = orderTableId;
		this.orderStatus = orderStatus;
		this.orderedTime = orderedTime;
		this.orderLineItemResponses = orderLineItemResponses;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;
		OrderResponse that = (OrderResponse) o;
		return Objects.equals(orderId, that.orderId) && Objects.equals(orderTableId, that.orderTableId) && Objects.equals(orderStatus, that.orderStatus) && Objects.equals(orderedTime, that.orderedTime);
	}

	@Override
	public int hashCode() {
		return Objects.hash(orderId, orderTableId, orderStatus, orderedTime);
	}

	public static OrderResponse of(Order order) {
		return new OrderResponse(
				order.getId(),
				order.getOrderTableId(),
				order.getOrderStatus(),
				order.getOrderedTime(),
				OrderLineItemResponse.of(order.getOrderLineItems()));
	}

	public static List<OrderResponse> of(List<Order> orders) {
		return orders.stream().map(OrderResponse::of)
				.collect(Collectors.toList());
	}

	public Long getOrderId() {
		return orderId;
	}

	public Long getOrderTableId() {
		return orderTableId;
	}

	public OrderStatus getOrderStatus() {
		return orderStatus;
	}

	public LocalDateTime getOrderedTime() {
		return orderedTime;
	}

	public List<OrderLineItemResponse> getOrderLineItemResponses() {
		return orderLineItemResponses;
	}
}
