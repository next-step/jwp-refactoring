package kitchenpos.table.domain;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public class OrderTables {

	private static final int ONE_TABLE_SIZE = 1;

	private List<OrderTable> orderTables = new ArrayList<>();

	protected OrderTables() {
	}

	private OrderTables(final List<OrderTable> orderTables) {
		this.orderTables = Collections.unmodifiableList(orderTables);
	}

	public static OrderTables from(final List<OrderTable> orderTables) {
		return new OrderTables(orderTables);
	}

	public List<OrderTable> getOrderTables() {
		return orderTables;
	}

	public List<Long> getOrderTablesIds() {
		return orderTables.stream()
			.map(OrderTable::getId)
			.collect(Collectors.toList());
	}

	public void changeTableGroup(Long tableGroupId) {
		orderTables.forEach(it -> {
			it.changeTableGroup(tableGroupId);
		});
	}

	public boolean isEmpty() {
		return orderTables.isEmpty();
	}

	public boolean findAnyNotEmptyTable() {
		return orderTables.stream()
			.anyMatch(it -> !it.isEmpty());
	}

	public boolean isOneTable() {
		return orderTables.size() == ONE_TABLE_SIZE;
	}

	public void unGroupOrderTables() {
		orderTables.forEach(OrderTable::unGroup);
	}
}
