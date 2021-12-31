package kitchenpos.table.dto;

import kitchenpos.table.domain.OrderTable;

public class TableResponse {

	private Long id;
	private int numberOfGuests;
	private boolean empty;

	public TableResponse() {
	}

	public TableResponse(Long id, int numberOfGuests, boolean empty) {
		this.id = id;
		this.numberOfGuests = numberOfGuests;
		this.empty = empty;
	}

	public TableResponse(OrderTable orderTable) {
		this.id = orderTable.getId();
		this.numberOfGuests = orderTable.getNumberOfGuests().toInt();
		this.empty = orderTable.isEmpty();
	}

	public Long getId() {
		return id;
	}

	public int getNumberOfGuests() {
		return numberOfGuests;
	}

	public boolean isEmpty() {
		return empty;
	}
}
