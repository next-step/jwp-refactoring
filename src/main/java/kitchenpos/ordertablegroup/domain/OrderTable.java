package kitchenpos.ordertablegroup.domain;

public class OrderTable {
	private Long id;
	private Long tableGroupId;
	private boolean empty;

	private OrderTable() {
	}

	public static OrderTable of(Long id, Long tableGroupId, boolean empty) {
		OrderTable orderTable = new OrderTable();
		orderTable.id = id;
		orderTable.tableGroupId = tableGroupId;
		orderTable.empty = empty;
		return orderTable;
	}

	public Long getId() {
		return id;
	}

	public Long getTableGroupId() {
		return tableGroupId;
	}

	public boolean isEmpty() {
		return empty;
	}

	public boolean hasOrderTableGroup() {
		return tableGroupId != null;
	}
}
