package kitchenpos.order.domain;

import java.util.List;
import java.util.Objects;

import javax.persistence.Embeddable;
import javax.persistence.OneToMany;

@Embeddable
public class OrderTables {

	@OneToMany(mappedBy = "tableGroup")
	private List<OrderTable> orderTables;

	protected OrderTables() {
	}

	private OrderTables(List<OrderTable> orderTables) {
		this.orderTables = orderTables;
	}

	public static OrderTables from(List<OrderTable> orderTables) {
		return new OrderTables(orderTables);
	}

	public List<OrderTable> list() {
		return orderTables;
	}

	public void updateGroup(TableGroup tableGroup) {
		orderTables.forEach(orderTable -> orderTable.updateGroup(tableGroup));
	}

	public void ungroup() {
		updateGroup(null);
	}

	public boolean alreadyOrderd() {
		return orderTables.stream().anyMatch(OrderTable::isOrdered);
	}

	public int size() {
		return orderTables.size();
	}

	public boolean emptyAndNoGroup() {
		return orderTables.stream()
			.allMatch(OrderTable::emptyAndNoGroup);
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) {
			return true;
		}
		if (o == null || getClass() != o.getClass()) {
			return false;
		}
		OrderTables that = (OrderTables)o;
		return Objects.equals(orderTables, that.orderTables);
	}

	@Override
	public int hashCode() {
		return Objects.hashCode(orderTables);
	}
}
