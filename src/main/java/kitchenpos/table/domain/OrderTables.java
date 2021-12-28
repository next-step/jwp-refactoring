package kitchenpos.table.domain;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.persistence.Embeddable;
import javax.persistence.FetchType;
import javax.persistence.OneToMany;

@Embeddable
public class OrderTables {

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "tableGroup")
	private List<OrderTable> orderTables = new ArrayList<>();

	protected OrderTables() {
	}

	private OrderTables(List<OrderTable> orderTables) {
		this.orderTables.addAll(orderTables);
	}

	public static OrderTables of(List<OrderTable> orderTables) {
		return new OrderTables(orderTables);
	}

	public void unGroup() {
		orderTables.forEach(OrderTable::unGroup);
	}

	public List<OrderTable> toList() {
		return Collections.unmodifiableList(orderTables);
	}

	@Override
	public boolean equals(Object o) {
		if (this == o)
			return true;
		if (o == null || getClass() != o.getClass())
			return false;

		OrderTables that = (OrderTables)o;

		return orderTables.equals(that.orderTables);
	}

	@Override
	public int hashCode() {
		return orderTables.hashCode();
	}

}
