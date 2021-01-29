package kitchenpos.table.domain;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import javax.persistence.Embeddable;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;

import kitchenpos.order.domain.Order;
import kitchenpos.order.domain.OrderStatus;

@Embeddable
public class TableOrders {
	@OneToMany
	@JoinColumn(name = "orderTableId")
	private List<Order> orders = new ArrayList<>();

	protected TableOrders() {
	}

	public TableOrders(List<Order> orders) {
		this.orders = orders;
	}

	public boolean isExistNoCompletion() {
		return Optional.ofNullable(orders)
			.orElseGet(Collections::emptyList)
			.stream()
			.map(Order::getOrderStatus)
			.anyMatch(it -> OrderStatus.COOKING.equals(it) || OrderStatus.MEAL.equals(it));
	}

}
