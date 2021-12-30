package kitchenpos.order.dto;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import kitchenpos.order.domain.domain.Order;
import kitchenpos.order.domain.domain.OrderStatus;
import kitchenpos.ordertable.dto.OrderTableResponse;

public class OrderResponse {

	private Long id;
	private OrderTableResponse orderTable;
	private OrderStatus orderStatus;
	private LocalDateTime orderedAt;
	private List<OrderLineItemResponse> orderLineItems;

	protected OrderResponse() {
	}

	private OrderResponse(Long id, OrderTableResponse orderTable, OrderStatus orderStatus, LocalDateTime orderedAt,
		List<OrderLineItemResponse> orderLineItems) {
		this.id = id;
		this.orderTable = orderTable;
		this.orderStatus = orderStatus;
		this.orderedAt = orderedAt;
		this.orderLineItems = orderLineItems;
	}

	public static OrderResponse of(Order order) {
		return new OrderResponse(
			order.getId(),
			OrderTableResponse.of(order.getOrderTable()),
			order.getOrderStatus(),
			order.getOrderedTime(),
			order.getOrderLineItems()
				.stream()
				.map(OrderLineItemResponse::of)
				.collect(Collectors.toList())
		);
	}

	public Long getId() {
		return id;
	}

	public OrderTableResponse getOrderTable() {
		return orderTable;
	}

	public OrderStatus getOrderStatus() {
		return orderStatus;
	}

	public LocalDateTime getOrderedAt() {
		return orderedAt;
	}

	public List<OrderLineItemResponse> getOrderLineItems() {
		return orderLineItems;
	}
}
