package kitchenpos.ordertable.dto;

import kitchenpos.ordertable.domain.NumberOfGuests;
import kitchenpos.ordertable.domain.OrderTable;

public class OrderTableRequest {

	private Long tableGroupId;
	private int numberOfGuests;

	public OrderTableRequest(Long tableGroupId, int numberOfGuests) {
		this.tableGroupId = tableGroupId;
		this.numberOfGuests = numberOfGuests;
	}

	public Long getTableGroupId() {
		return tableGroupId;
	}

	public int getNumberOfGuests() {
		return numberOfGuests;
	}

	public OrderTable toOrderTable() {
		return new OrderTable(new NumberOfGuests(numberOfGuests));
	}
}
