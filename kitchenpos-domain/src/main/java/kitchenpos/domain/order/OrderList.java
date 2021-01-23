package kitchenpos.domain.order;

import java.util.List;
import java.util.Optional;

public class OrderList {
	private List<Orders> orderList;

	public OrderList(List<Orders> orderList) {
		this.orderList = orderList;
	}

	public boolean isCompleteAllOrders() {
		Optional<Orders> notCompleteOrder = this.orderList.stream()
				.filter(order -> !OrderStatus.COMPLETION.name().equals(order.getOrderStatus()))
				.findFirst();

		return !notCompleteOrder.isPresent();
	}
}
