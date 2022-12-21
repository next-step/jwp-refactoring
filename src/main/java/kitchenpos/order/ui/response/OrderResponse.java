package kitchenpos.order.ui.response;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import kitchenpos.order.domain.Order;

public class OrderResponse {

	private final long id;
	private final long orderTableId;
	private final String orderStatus;
	private final LocalDateTime orderedTime;
	private final List<OrderLineItemResponse> orderLineItems;

	private OrderResponse(long id, long orderTableId, String orderStatus, LocalDateTime orderedTime,
		List<OrderLineItemResponse> orderLineItems) {
		this.id = id;
		this.orderTableId = orderTableId;
		this.orderStatus = orderStatus;
		this.orderedTime = orderedTime;
		this.orderLineItems = orderLineItems;
	}

	public static OrderResponse of(long id, long orderTableId, String orderStatus, LocalDateTime orderedTime,
		List<OrderLineItemResponse> orderLineItems) {
		return new OrderResponse(id, orderTableId, orderStatus, orderedTime, orderLineItems);
	}

	public static OrderResponse from(Order order) {
		return new OrderResponse(
			order.id(),
			order.table().id(),
			order.orderStatus().name(),
			order.orderedTime(),
			OrderLineItemResponse.listFrom(order.orderLineItems())
		);
	}

	public static List<OrderResponse> listFrom(List<Order> orders) {
		return orders.stream()
			.map(OrderResponse::from)
			.collect(Collectors.toList());
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
