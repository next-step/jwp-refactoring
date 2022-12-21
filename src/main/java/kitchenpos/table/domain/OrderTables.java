package kitchenpos.table.domain;

import java.util.Collections;
import java.util.List;

import kitchenpos.exception.ErrorMessage;

public class OrderTables {
	private static int MIN_GROUP_SIZE = 2;
	private final List<OrderTable> orderTables;

	private OrderTables(List<OrderTable> orderTables) {
		this.orderTables = Collections.unmodifiableList(orderTables);
	}

	public static OrderTables of(List<OrderTable> orderTables) {
		return new OrderTables(orderTables);
	}

	public List<OrderTable> value() {
		return orderTables;
	}

	public void group(Long tableGroupId) {
		validateSize();
		this.orderTables.forEach(it -> it.group(tableGroupId));
	}

	public void unGroup() {
		this.orderTables.forEach(it -> it.unGroup());
	}

	private void validateSize() {
		if (orderTables.size() < MIN_GROUP_SIZE) {
			throw new IllegalArgumentException(ErrorMessage.CANNOT_TABLE_GROUP_WHEN_SIZE_SIZE_IS_TOO_SMALL);
		}
	}

}
