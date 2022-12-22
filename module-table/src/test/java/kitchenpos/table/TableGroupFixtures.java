package kitchenpos.table;

import java.util.stream.Collectors;
import java.util.stream.LongStream;

import org.assertj.core.util.Lists;

import kitchenpos.table.domain.OrderTable;
import kitchenpos.table.domain.OrderTables;
import kitchenpos.table.domain.TableGroup;

public class TableGroupFixtures {

	public static OrderTables anEmptyOrderTables(long count) {
		return anOrderTables(count, true);
	}

	public static OrderTables anOccupiedOrderTables(long count) {
		return anOrderTables(count, false);
	}

	private static OrderTables anOrderTables(long count, boolean isEmpty) {
		return new OrderTables(LongStream.range(0, count)
										 .mapToObj(id -> createOrderTable(id, 1, isEmpty))
										 .collect(Collectors.toList()));
	}

	public static OrderTable createOrderTable(long id, int numberOfGuests, boolean isEmpty) {
		return new OrderTable(id, numberOfGuests, isEmpty);
	}

	public static OrderTables aGroupedOrderTables() {
		return new OrderTables(Lists.newArrayList(
			aGroupedOrderTable(),
			aGroupedOrderTable()));
	}

	public static OrderTable aGroupedOrderTable() {
		return new OrderTable(1L, new TableGroup(Lists.newArrayList()), 1, true);
	}
}
