package domain.order;

import org.hibernate.annotations.BatchSize;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;

import javax.persistence.Embeddable;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Objects;

@Embeddable
class TableOrders implements Iterable<Order> {

	@Fetch(FetchMode.SELECT)
	@BatchSize(size = 10)
	@OneToMany
	@JoinColumn(name = "order_table_id", nullable = false)
	private List<Order> orders;

	TableOrders() {
		this.orders = new ArrayList<>();
	}

	Order createNewOrder(long orderTableId, List<OrderItem> items) {
		Order order = Order.createCookingOrder(orderTableId, items);
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

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (!(o instanceof TableOrders)) return false;
		TableOrders orders1 = (TableOrders) o;
		return Objects.equals(orders, orders1.orders);
	}

	@Override
	public int hashCode() {
		return Objects.hash(orders);
	}
}
