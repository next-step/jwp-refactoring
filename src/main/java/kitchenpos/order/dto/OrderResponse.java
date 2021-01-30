package kitchenpos.order.dto;

import java.util.List;
import java.util.stream.Collectors;

import kitchenpos.order.domain.Order;

public class OrderResponse {
	private Long id;
	private OrderTableResponse orderTable;
	private String orderStatus;
	private List<OrderLineItemResponse> orderLineItems;

	protected OrderResponse() {
	}

	public OrderResponse(Long id, OrderTableResponse orderTable, String orderStatus,
		List<OrderLineItemResponse> orderLineItems) {
		this.id = id;
		this.orderTable = orderTable;
		this.orderStatus = orderStatus;
		this.orderLineItems = orderLineItems;
	}

	public static OrderResponse of(Order order) {
		return new OrderResponse(order.getId(), OrderTableResponse.of(order.getOrderTable()),
			order.getOrderStatus(), OrderLineItemResponse.of(order.getOrderLineItems()));
	}

	public static List<OrderResponse> of(List<Order> orders) {
		return orders.stream()
			.map(OrderResponse::of)
			.collect(Collectors.toList());
	}

	public Long getId() {
		return id;
	}

	public OrderTableResponse getOrderTable() {
		return orderTable;
	}

	public String getOrderStatus() {
		return orderStatus;
	}

	public List<OrderLineItemResponse> getOrderLineItems() {
		return orderLineItems;
	}
}
