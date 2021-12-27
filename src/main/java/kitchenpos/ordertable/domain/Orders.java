package kitchenpos.ordertable.domain;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Embeddable;
import javax.persistence.FetchType;
import javax.persistence.OneToMany;

import kitchenpos.orders.domain.Order;

@Embeddable
public class Orders {
	@OneToMany(fetch = FetchType.LAZY, mappedBy = "orderTable")
	private final List<Order> orders;


	public Orders() {
		this.orders = new ArrayList<>();
	}
	public Orders(List<Order> orders) {
		this.orders = orders;
	}

	public List<Order> value() {
		return new ArrayList<>(orders);
	}

	public boolean isOrderCompletion() {
		return orders.stream()
			.allMatch(Order::isCompletion);
	}
}
