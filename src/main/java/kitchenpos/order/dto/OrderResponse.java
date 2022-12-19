package kitchenpos.order.dto;

import java.time.LocalDateTime;
import java.util.List;

import kitchenpos.order.domain.Order;
import kitchenpos.order.domain.OrderLineItems;
import kitchenpos.order.domain.OrderStatus;

public class OrderResponse {

	private Long id;
	private Long orderTableId;
	private LocalDateTime orderedTime;

	private OrderStatus orderStatus;
	private List<OrderLineItemResponse> orderLineItemResponses;

	protected OrderResponse() {}

	private OrderResponse(Long id, Long orderTableId, OrderStatus orderStatus, LocalDateTime orderedTime,
		OrderLineItems orderLineItems) {
		this.id = id;
		this.orderTableId = orderTableId;
		this.orderStatus = orderStatus;
		this.orderedTime = orderedTime;
		this.orderLineItemResponses = OrderLineItemResponse.of(orderLineItems);
	}

	public static OrderResponse of(Order order) {
		return new OrderResponse(order.getId(),
			order.getOrderTableId(),
			order.getOrderStatus(),
			order.getOrderedTime(),
			order.getOrderLineItems());
	}

	public Long getId() {
		return id;
	}

	public Long getOrderTableId() {
		return orderTableId;
	}

	public LocalDateTime getOrderedTime() {
		return orderedTime;
	}

	public OrderStatus getOrderStatus() {
		return orderStatus;
	}

	public List<OrderLineItemResponse> getOrderLineItemResponses() {
		return orderLineItemResponses;
	}
}
