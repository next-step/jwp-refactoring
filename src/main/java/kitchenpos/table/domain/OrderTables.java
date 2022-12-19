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
		validateGrouped();
		validateEmpty();
		validateSize();
		this.orderTables.forEach(it -> it.groupBy(tableGroupId));
	}

	public void unGroup() {
		this.orderTables.forEach(it -> it.unGroup());
	}

	private void validateSize() {
		if (orderTables.size() < MIN_GROUP_SIZE) {
			throw new IllegalArgumentException(ErrorMessage.CANNOT_TABLE_GROUP_WHEN_SIZE_SIZE_IS_TOO_SMALL);
		}
	}

	private void validateEmpty() {
		if (!isAllEmpty()) {
			throw new IllegalArgumentException(ErrorMessage.CANNOT_TABLE_GROUP_WHEN_IS_NOT_ALL_EMPTY);
		}
	}

	private void validateGrouped() {
		if (isAnyGrouped()) {
			throw new IllegalArgumentException(ErrorMessage.CANNOT_TABLE_GROUP_WHEN_ALREADY_GROUPED);
		}
	}

	private boolean isAllEmpty() {
		return orderTables.stream().allMatch(it -> it.isEmpty());
	}

	private boolean isAnyGrouped() {
		return orderTables.stream()
			.anyMatch(it -> it.isGrouped());
	}

}
