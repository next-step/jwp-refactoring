package kitchenpos.order.dto;

import java.util.List;

import kitchenpos.order.domain.Order;
import kitchenpos.order.domain.OrderLineItems;
import kitchenpos.ordertable.domain.OrderTable;

public class OrderStatusChangeRequest {

	private String orderStatus;

	public OrderStatusChangeRequest(String orderStatus) {
		this.orderStatus = orderStatus;
	}

	public String getOrderStatus() {
		return orderStatus;
	}
}
