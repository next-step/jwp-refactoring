package kitchenpos.table.domain;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

import javax.persistence.CascadeType;
import javax.persistence.Embeddable;
import javax.persistence.FetchType;
import javax.persistence.OneToMany;

@Embeddable
public class OrderTables {

	@OneToMany(mappedBy = "tableGroup", fetch = FetchType.LAZY, cascade = CascadeType.PERSIST)
	private List<OrderTable> orderTables = new ArrayList<>();

	protected OrderTables() {
	}

	public OrderTables(List<OrderTable> orderTables) {
		this.orderTables = orderTables;
	}

	public void addAll(List<OrderTable> addedOrderTables) {
		orderTables.addAll(addedOrderTables);
	}

	public Stream<OrderTable> stream() {
		return orderTables.stream();
	}

	public void ungroup() {
		orderTables.forEach(OrderTable::detachTableGroup);
		orderTables.clear();
	}

	public List<OrderTable> toList() {
		return orderTables;
	}

	public int size() {
		return orderTables.size();
	}

	public boolean isEmpty() {
		return orderTables.isEmpty();
	}

	public void addIfNotExists(OrderTable orderTable) {
		if (orderTables.contains(orderTable)) {
			return;
		}
		orderTables.add(orderTable);
	}

	public void remove(OrderTable orderTable) {
		orderTables.remove(orderTable);
	}
}
