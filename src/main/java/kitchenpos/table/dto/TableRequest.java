package kitchenpos.table.dto;

import kitchenpos.table.domain.NumberOfGuests;
import kitchenpos.table.domain.OrderTable;

public class TableRequest {

	private int numberOfGuests;
	private boolean empty;

	public TableRequest() {
	}

	public TableRequest(int numberOfGuests, boolean empty) {
		this.numberOfGuests = numberOfGuests;
		this.empty = empty;
	}

	public OrderTable toEntity() {
		return OrderTable.of(NumberOfGuests.of(numberOfGuests), empty);
	}

	public int getNumberOfGuests() {
		return numberOfGuests;
	}

	public boolean isEmpty() {
		return empty;
	}

}
