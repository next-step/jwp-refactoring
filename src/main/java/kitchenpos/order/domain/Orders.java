package kitchenpos.order.domain;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Embeddable;
import javax.persistence.OneToMany;

@Embeddable
public class Orders {
	@OneToMany(mappedBy = "orderTable", cascade = CascadeType.PERSIST, orphanRemoval = true)
	private List<Order> orders = new ArrayList<>();

	protected Orders() {
	}

	private Orders(List<Order> orders) {
		this.orders = orders;
	}

	public static Orders of(List<Order> orders) {
		return new Orders(orders);
	}

	public boolean isAllFinished() {
		return orders.stream().allMatch(it -> it.isFinished());
	}
}
