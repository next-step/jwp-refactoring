package kitchenpos.table;

import java.util.List;

import org.assertj.core.util.Lists;

import kitchenpos.table.domain.OrderTable;

public class OrderTableFixtures {

	public static OrderTable createOrderTable(long id, int numberOfGuests, boolean empty) {
		return new OrderTable(id, numberOfGuests, empty);
	}

	public static OrderTable createOrderTable(long id, boolean empty) {
		return new OrderTable(id, 1, empty);
	}

	public static OrderTable createOrderTable() {
		return createOrderTable(1L, false);
	}

	public static List<OrderTable> createOrderTables() {
		return Lists.newArrayList(
			createOrderTable(1L, false),
			createOrderTable(2L, false),
			createOrderTable(3L, false)
		);
	}

	public static OrderTable numberOfGuests(int numberOfGuests) {
		return new OrderTable(1L, numberOfGuests, true);
	}

	public static OrderTable notEmptyOrderTable(int numberOfGuests) {
		return new OrderTable(1L, numberOfGuests, false);
	}
}
