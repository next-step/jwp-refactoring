package kitchenpos.table.table.ui.request;

import kitchenpos.table.table.domain.NumberOfGuests;
import kitchenpos.table.table.domain.OrderTable;
import kitchenpos.table.table.domain.TableEmpty;

public class OrderTableRequest {
	private final int numberOfGuests;
	private final boolean empty;

	public OrderTableRequest(int numberOfGuests, boolean empty) {
		this.numberOfGuests = numberOfGuests;
		this.empty = empty;
	}

	public int getNumberOfGuests() {
		return numberOfGuests;
	}

	public boolean isEmpty() {
		return empty;
	}

	public OrderTable toEntity() {
		return OrderTable.of(NumberOfGuests.from(numberOfGuests), TableEmpty.from(empty));
	}
}
