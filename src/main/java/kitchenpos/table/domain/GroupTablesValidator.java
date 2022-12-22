package kitchenpos.table.domain;

import static kitchenpos.table.exception.CannotCreateGroupTableException.TYPE.HAS_GROUP_TABLE;
import static kitchenpos.table.exception.CannotCreateGroupTableException.TYPE.HAS_NO_ORDER_TABLE;
import static kitchenpos.table.exception.CannotCreateGroupTableException.TYPE.INVALID_TABLE_COUNT;
import static kitchenpos.table.exception.CannotCreateGroupTableException.TYPE.NOT_EMPTY_ORDER_TABLE;

import kitchenpos.annoation.DomainService;
import kitchenpos.table.exception.CannotCreateGroupTableException;

@DomainService
public class GroupTablesValidator {

	private static final int MINIMUM_ORDER_TABLE_COUNT = 2;

	public void validate(OrderTables orderTables) {
		if (hasNoOrderTables(orderTables)) {
			throw new CannotCreateGroupTableException(HAS_NO_ORDER_TABLE);
		}
		if (isOrderTablesValidSize(orderTables)) {
			throw new CannotCreateGroupTableException(INVALID_TABLE_COUNT);
		}
		if (isOrderTableNotEmpty(orderTables)) {
			throw new CannotCreateGroupTableException(NOT_EMPTY_ORDER_TABLE);
		}
		if (hasOrderTableGroup(orderTables)) {
			throw new CannotCreateGroupTableException(HAS_GROUP_TABLE);
		}
	}

	private boolean hasOrderTableGroup(OrderTables orderTables) {
		return orderTables.stream()
			.anyMatch(OrderTable::hasTableGroup);
	}

	private boolean isOrderTableNotEmpty(OrderTables orderTables) {
		return orderTables.stream()
			.noneMatch(OrderTable::isEmpty);
	}

	private boolean isOrderTablesValidSize(OrderTables orderTables) {
		return orderTables.size() < MINIMUM_ORDER_TABLE_COUNT;
	}

	private boolean hasNoOrderTables(OrderTables orderTables) {
		return orderTables.isEmpty();
	}
}
