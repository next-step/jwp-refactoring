package kitchenpos.order.dto;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import kitchenpos.order.domain.Order;
import kitchenpos.order.domain.OrderStatus;
import kitchenpos.table.domain.OrderTable;

public class OrderResponse {
	private Long id;
	private Long orderTableId;
	private OrderStatus orderStatus;
	private LocalDateTime orderedTime;
	private List<OrderLineItemResponse> orderLineItems;

	public OrderResponse() {
	}

	public OrderResponse(Long id, Long orderTableId, OrderStatus orderStatus, LocalDateTime orderedTime,
		List<OrderLineItemResponse> orderLineItems) {
		this.id = id;
		this.orderTableId = orderTableId;
		this.orderStatus = orderStatus;
		this.orderedTime = orderedTime;
		this.orderLineItems = orderLineItems;
	}

	public static OrderResponse from(Order order){
		Long orderTableId = Optional.ofNullable(order.getOrderTable())
			.map(OrderTable::getId)
			.orElse(null);
		return new OrderResponse(
			order.getId(),
			orderTableId,
			order.getOrderStatus(),
			order.getOrderedTime(),
			OrderLineItemResponse.newList(order.getOrderLineItems())
		);
	}

	public static List<OrderResponse> newList(List<Order> orders) {
		return orders.stream()
			.map(OrderResponse::from)
			.collect(Collectors.toList());
	}

	public Long getId() {
		return id;
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

	public List<OrderLineItemResponse> getOrderLineItems() {
		return orderLineItems;
	}
}
