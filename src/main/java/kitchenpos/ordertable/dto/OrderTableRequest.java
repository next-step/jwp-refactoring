package kitchenpos.ordertable.dto;

import kitchenpos.ordertable.domain.OrderTable;

public class OrderTableRequest {

	private Long id;
	private Long tableGroupId;
	private int numberOfGuests;
	private boolean empty;

	protected OrderTableRequest() {
	}

	protected OrderTableRequest(final Long tableGroupId, final int numberOfGuests, final boolean empty) {
		this.tableGroupId = tableGroupId;
		this.numberOfGuests = numberOfGuests;
		this.empty = empty;
	}

	public OrderTableRequest(final Long id) {
		this(null, 0, false);
		this.id = id;
	}

	public OrderTableRequest(final int numberOfGuests, final boolean empty) {
		this(null, numberOfGuests, empty);
	}

	public Long getId() {
		return id;
	}

	public void setId(final Long id) {
		this.id = id;
	}

	public Long getTableGroupId() {
		return tableGroupId;
	}

	public void setTableGroupId(final Long tableGroupId) {
		this.tableGroupId = tableGroupId;
	}

	public int getNumberOfGuests() {
		return numberOfGuests;
	}

	public void setNumberOfGuests(final int numberOfGuests) {
		this.numberOfGuests = numberOfGuests;
	}

	public boolean isEmpty() {
		return empty;
	}

	public void setEmpty(final boolean empty) {
		this.empty = empty;
	}

	public OrderTable toOrderTable() {
		return new OrderTable(numberOfGuests, empty);
	}

}
