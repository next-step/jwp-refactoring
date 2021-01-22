package kitchenpos.order.domain;

import org.hibernate.annotations.BatchSize;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;

import javax.persistence.Embeddable;
import javax.persistence.OneToMany;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

@Embeddable
class TableOrders implements Iterable<Order> {

	@Fetch(FetchMode.SELECT)
	@BatchSize(size = 10)
	@OneToMany(mappedBy = "orderTable")
	private List<Order> orders;

	TableOrders() {
		this.orders = new ArrayList<>();
	}

	Order createNewOrder(OrderTable orderTable, List<OrderItem> items) {
		Order order = Order.createCookingOrder(orderTable, items);
		orders.add(order);
		return order;
	}
	
	boolean hasOngoingOrder() {
		return orders.stream().anyMatch(Order::isOngoing);
	}

	@Override
	public Iterator<Order> iterator() {
		return orders.listIterator();
	}
}
