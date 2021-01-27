package api.order.dto;


import domain.order.OrderStatus;

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
