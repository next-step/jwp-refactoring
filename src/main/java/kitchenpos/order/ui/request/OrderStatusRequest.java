package kitchenpos.order.ui.request;

import kitchenpos.order.domain.OrderStatus;

public class OrderStatusRequest {

	private final String orderStatus;

	public OrderStatusRequest(String orderStatus) {
		this.orderStatus = orderStatus;
	}

	public String getOrderStatus() {
		return orderStatus;
	}

	public OrderStatus status() {
		return OrderStatus.valueOf(orderStatus);
	}
}
