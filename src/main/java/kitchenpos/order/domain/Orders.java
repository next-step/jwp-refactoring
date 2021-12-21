package kitchenpos.order.domain;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Embeddable;
import javax.persistence.FetchType;
import javax.persistence.OneToMany;

@Embeddable
public class Orders {

	@OneToMany(mappedBy = "orderTable", fetch = FetchType.LAZY)
	List<Order> orders = new ArrayList<>();

	protected Orders() {
	}

	private Orders(List<Order> orders) {
		this.orders = orders;
	}

	public static Orders from(List<Order> orders) {
		return new Orders(orders);
	}

	public List<Order> getOrders() {
		return orders;
	}

	public void addOrder(Order order) {
		this.orders.add(order);
	}

	public void hasNotCompletionOrder() {
		orders.forEach(Order::isNotCompletion);
	}
}
