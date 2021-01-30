package kitchenpos.order.domain;

import java.util.ArrayList;
import java.util.List;

public class Orders {
	private List<Order> orders = new ArrayList<>();

	public Orders(List<Order> orders) {
		this.orders = orders;
	}

	public boolean containsOderStatuses(List<String> orderStatuses) {
		return orders.stream()
			.anyMatch(order -> order.containsOrderStatus(orderStatuses));

	}
}
