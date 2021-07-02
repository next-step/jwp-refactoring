package kitchenpos.domain;

import static java.util.Arrays.*;

import java.util.Collections;
import java.util.List;

import javax.persistence.Embeddable;
import javax.persistence.OneToMany;

@Embeddable
public class OrderTables {

	@OneToMany(mappedBy = "tableGroup")
	private List<OrderTable> orderTables;

	protected OrderTables() {}

	private OrderTables(List<OrderTable> orderTables) {
		this.orderTables = orderTables;
	}

	public static OrderTables of(OrderTable... orderTables) {
		return new OrderTables(asList(orderTables));
	}

	public static OrderTables of(List<OrderTable> orderTables) {
		return new OrderTables(orderTables);
	}

	public void groupBy(TableGroup tableGroup) {
		for (OrderTable orderTable : orderTables) {
			orderTable.setTableGroup(tableGroup);
		}
	}

	public int size() {
		return orderTables.size();
	}

	public boolean containsNotEmptyTable() {
		return orderTables.stream()
			.anyMatch(OrderTable::isNotEmpty);
	}

	public boolean containsGroupedOrderTables() {
		return orderTables.stream()
			.anyMatch(OrderTable::isGrouped);
	}

	List<OrderTable> getOrderTables() {
		return Collections.unmodifiableList(orderTables);
	}
}
