package kitchenpos.ui.dto;

import kitchenpos.domain.OrderStatus;

public class OrderStatusRequest {

	private String orderStatus;

	private OrderStatusRequest() {
	}

	public OrderStatusRequest(String orderStatus) {
		this.orderStatus = orderStatus;
	}

	public String getOrderStatus() {
		return orderStatus;
	}

	public OrderStatus toOrderStatus() {
		return OrderStatus.valueOf(orderStatus.toUpperCase());
	}
}
