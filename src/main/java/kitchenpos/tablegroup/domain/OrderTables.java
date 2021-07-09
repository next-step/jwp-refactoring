package kitchenpos.tablegroup.domain;

import static java.util.Arrays.*;

import java.util.Collections;
import java.util.List;

import kitchenpos.table.domain.OrderTable;
import kitchenpos.table.domain.TableGroupId;

public class OrderTables {
	private static final int MIN_TABLE_COUNT = 2;
	private final List<OrderTable> tables;

	private OrderTables(List<OrderTable> tables) {
		this.tables = tables;
	}

	public static OrderTables of(List<OrderTable> orderTables) {
		return new OrderTables(orderTables);
	}

	public static OrderTables of(OrderTable... orderTables) {
		return new OrderTables(asList(orderTables));
	}

	public List<OrderTable> getTables() {
		return Collections.unmodifiableList(tables);
	}

	void grouped(TableGroupId tableGroupId) {
		validateMinTableSize();
		validateNoGroupedTables();
		validateNoEmptyTables();
		for (OrderTable orderTable : tables) {
			orderTable.grouped(tableGroupId);
		}
	}

	void ungrouped(UngroupValidator ungroupValidator) {
		for (OrderTable orderTable : tables) {
			orderTable.ungrouped(ungroupValidator);
		}
	}

	private void validateNoGroupedTables() {
		boolean containsGroupedOrderTables = tables.stream().anyMatch(OrderTable::isGrouped);
		if (containsGroupedOrderTables) {
			throw new IllegalArgumentException("이미 그룹화되어 있는 테이블은 그룹화 할 수 없습니다.");
		}
	}

	private void validateNoEmptyTables() {
		boolean isEmpty = tables.stream().allMatch(OrderTable::isEmpty);
		if (!isEmpty) {
			throw new IllegalArgumentException("비어있지 않은 테이블은 그룹화 할 수 없습니다.");
		}
	}

	private void validateMinTableSize() {
		if (tables.size() < MIN_TABLE_COUNT) {
			throw new IllegalArgumentException("2개 미만의 주문테이블은 그룹화 할 수 없습니다.");
		}
	}
}
