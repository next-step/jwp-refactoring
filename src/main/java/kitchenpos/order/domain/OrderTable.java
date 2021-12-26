package kitchenpos.order.domain;

public class OrderTable {
	private boolean empty;

	private OrderTable() {
	}

	public static OrderTable from(boolean empty) {
		OrderTable orderTable = new OrderTable();
		orderTable.empty = empty;
		return orderTable;
	}

	public boolean isEmpty() {
		return empty;
	}
}
