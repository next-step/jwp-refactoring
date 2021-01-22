package kitchenpos.order.dto;


import kitchenpos.order.domain.OrderStatus;

public class OrderRequest_ChangeStatus {

	private OrderStatus orderStatus;

	public OrderRequest_ChangeStatus() {
	}

	public OrderRequest_ChangeStatus(OrderStatus orderStatus) {
		this.orderStatus = orderStatus;
	}

	public OrderStatus getOrderStatus() {
		return orderStatus;
	}
}
