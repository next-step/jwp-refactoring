package kitchenpos.dto;

public class OrderRequest_ChangeStatus {

	// TODO: 2021-01-19 changeToEnum
	private String orderStatus;

	public OrderRequest_ChangeStatus() {
	}

	public OrderRequest_ChangeStatus(String orderStatus) {
		this.orderStatus = orderStatus;
	}

	public String getOrderStatus() {
		return orderStatus;
	}
}
