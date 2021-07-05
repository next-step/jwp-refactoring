package kitchenpos.domain;

import static java.util.Arrays.*;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import javax.persistence.Embeddable;
import javax.persistence.OneToMany;

@Embeddable
class OrderTables {
	private static final int MIN_TABLE_COUNT = 2;

	@OneToMany(mappedBy = "tableGroup")
	private List<OrderTable> orderTables;

	protected OrderTables() {}

	private OrderTables(List<OrderTable> orderTables) {
		this.orderTables = orderTables;
	}

	public static OrderTables of(List<OrderTable> orderTables) {
		validateMinTableSize(orderTables);
		validateNoGroupedTables(orderTables);
		validateNoEmptyTables(orderTables);
		return new OrderTables(orderTables);
	}

	void toGroup(TableGroup tableGroup) {
		for (OrderTable orderTable : orderTables) {
			orderTable.toGroup(tableGroup);
		}
	}

	List<OrderTable> getOrderTables() {
		return Collections.unmodifiableList(orderTables);
	}

	List<Long> getOrderTableIds() {
		return orderTables.stream()
			.map(OrderTable::getId)
			.collect(Collectors.toList());
	}

	void ungrouped() {
		for (OrderTable orderTable : orderTables) {
			orderTable.ungrouped();
		}
	}

	private static void validateNoGroupedTables(List<OrderTable> orderTables) {
		boolean containsGroupedOrderTables = orderTables.stream().anyMatch(OrderTable::isGrouped);
		if (containsGroupedOrderTables) {
			throw new IllegalArgumentException("이미 그룹화되어 있는 테이블은 그룹화 할 수 없습니다.");
		}
	}

	private static void validateNoEmptyTables(List<OrderTable> orderTables) {
		boolean containsNotEmptyTable = orderTables.stream().anyMatch(OrderTable::isNotEmpty);
		if (containsNotEmptyTable) {
			throw new IllegalArgumentException("비어있지 않은 테이블은 그룹화 할 수 없습니다.");
		}
	}

	private static void validateMinTableSize(List<OrderTable> orderTables) {
		if (orderTables.size() < MIN_TABLE_COUNT) {
			throw new IllegalArgumentException("2개 미만의 주문테이블은 그룹화 할 수 없습니다.");
		}
	}
}
