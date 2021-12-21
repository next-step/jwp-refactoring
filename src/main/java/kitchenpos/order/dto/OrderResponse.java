package kitchenpos.order.dto;

import java.util.List;
import java.util.stream.Collectors;

import kitchenpos.order.domain.Order;
import kitchenpos.order.domain.OrderStatus;

public class OrderResponse {

	private Long id;
	private OrderTableResponse orderTable;
	private OrderStatus orderStatus;
	private List<OrderLineItemResponse> orderLineItems;

	protected OrderResponse() {
	}

	private OrderResponse(Long id, OrderTableResponse orderTable, OrderStatus orderStatus,
		List<OrderLineItemResponse> orderLineItems) {
		this.id = id;
		this.orderTable = orderTable;
		this.orderStatus = orderStatus;
		this.orderLineItems = orderLineItems;
	}

	public static OrderResponse of(Long id, OrderTableResponse orderTable,
		OrderStatus orderStatus, List<OrderLineItemResponse> orderLineItems) {
		return new OrderResponse(id, orderTable, orderStatus, orderLineItems);
	}

	public static List<OrderResponse> ofList(List<Order> orders) {
		return orders.stream()
			.map(Order::toResDto)
			.collect(Collectors.toList());
	}

	public Long getId() {
		return id;
	}

	public OrderStatus getOrderStatus() {
		return orderStatus;
	}

	public OrderTableResponse getOrderTable() {
		return orderTable;
	}

	public List<OrderLineItemResponse> getOrderLineItems() {
		return orderLineItems;
	}
}
