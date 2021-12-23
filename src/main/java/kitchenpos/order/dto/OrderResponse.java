package kitchenpos.order.dto;

import java.util.List;
import java.util.stream.Collectors;

import kitchenpos.order.domain.Order;
import kitchenpos.order.domain.OrderStatus;

public class OrderResponse {

	private Long id;
	private Long orderTableId;
	private OrderStatus orderStatus;
	private List<OrderLineItemResponse> orderLineItems;

	protected OrderResponse() {
	}

	private OrderResponse(Long id, Long orderTableId, OrderStatus orderStatus,
		List<OrderLineItemResponse> orderLineItems) {
		this.id = id;
		this.orderTableId = orderTableId;
		this.orderStatus = orderStatus;
		this.orderLineItems = orderLineItems;
	}

	public static OrderResponse of(Long id, Long orderTableId,
		OrderStatus orderStatus, List<OrderLineItemResponse> orderLineItems) {
		return new OrderResponse(id, orderTableId, orderStatus, orderLineItems);
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

	public Long getOrderTableId() {
		return orderTableId;
	}

	public List<OrderLineItemResponse> getOrderLineItems() {
		return orderLineItems;
	}
}
