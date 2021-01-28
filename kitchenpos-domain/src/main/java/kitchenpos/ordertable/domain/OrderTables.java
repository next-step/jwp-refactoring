package kitchenpos.ordertable.domain;

import java.util.List;
import java.util.stream.Stream;

import kitchenpos.tablegroup.domain.TableGroup;

public class OrderTables {

	private final List<OrderTable> orderTables;

	private OrderTables(final List<OrderTable> orderTables) {
		this.orderTables = orderTables;
	}

	public static OrderTables of(final List<OrderTable> orderTables) {
		return new OrderTables(orderTables);
	}

	public int size() {
		return this.orderTables.size();
	}

	public void validateIsGroup() {
		this.orderTables.forEach(OrderTable::validateIsGroup);
	}

	public void createGroup(TableGroup tableGroup) {
		this.orderTables.forEach(it -> it.createGroup(tableGroup));
	}

	public Stream<OrderTable> stream() {
		return this.orderTables.stream();
	}

	public void clearTableGroup() {
		this.orderTables.forEach(OrderTable::clearTableGroup);
	}

	public void validateNotEmpty() {
		this.orderTables.forEach(OrderTable::validateNotEmpty);
	}

	public List<OrderTable> getOrderTables() {
		return orderTables;
	}
}
