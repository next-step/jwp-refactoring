package kitchenpos.dto.order;


import kitchenpos.domain.order.Orders;

import java.util.List;
import java.util.stream.Collectors;

public class OrderResponse {
	private Long id;
	private Long orderTableId;
	private String orderStatus;
	private List<OrderLineItemResponse> orderLineItems;

	public OrderResponse(Long id, Long orderTableId, String orderStatus, List<OrderLineItemResponse> orderLineItems) {
		this.id = id;
		this.orderTableId = orderTableId;
		this.orderStatus = orderStatus;
		this.orderLineItems = orderLineItems;
	}

	public static OrderResponse of(Orders savedOrders) {
		return new OrderResponse(savedOrders.getId(), savedOrders.getOrderTable().getId(),
				savedOrders.getOrderStatus(), null);
	}

	public static List<OrderResponse> of(List<Orders> orders) {
		return orders.stream().map(OrderResponse::of).collect(Collectors.toList());
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Long getOrderTableId() {
		return orderTableId;
	}

	public void setOrderTableId(Long orderTableId) {
		this.orderTableId = orderTableId;
	}

	public String getOrderStatus() {
		return orderStatus;
	}

	public void setOrderStatus(String orderStatus) {
		this.orderStatus = orderStatus;
	}

	public List<OrderLineItemResponse> getOrderLineItems() {
		return orderLineItems;
	}

	public void setOrderLineItems(List<OrderLineItemResponse> orderLineItems) {
		this.orderLineItems = orderLineItems;
	}
}
