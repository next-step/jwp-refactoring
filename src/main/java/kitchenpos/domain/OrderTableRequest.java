package kitchenpos.domain;

import java.util.Objects;

public class OrderTableRequest {
    private Long id;
    private Long tableGroupId;
    private int numberOfGuests;
    private boolean empty;

    public OrderTableRequest() {
	}

	public OrderTableRequest(boolean empty) {
		this.empty = empty;
	}

	public OrderTableRequest(int numberOfGuests) {
    	this.numberOfGuests = numberOfGuests;
	}

	public OrderTableRequest(int numberOfGuests, boolean empty) {
		this.numberOfGuests = numberOfGuests;
		this.empty = empty;
	}

	public OrderTableRequest(Long tableGroupId, int numberOfGuests, boolean empty) {
		this.tableGroupId = tableGroupId;
		this.numberOfGuests = numberOfGuests;
		this.empty = empty;
	}

	public OrderTableRequest(Long id, Long tableGroupId, int numberOfGuests, boolean empty) {
    	this.id = id;
    	this.tableGroupId = tableGroupId;
    	this.numberOfGuests = numberOfGuests;
    	this.empty = empty;
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

	@Override
	public boolean equals(Object o) {
		if (this == o)
			return true;
		if (o == null || getClass() != o.getClass())
			return false;
		OrderTableRequest that = (OrderTableRequest)o;
		return numberOfGuests == that.numberOfGuests &&
			empty == that.empty &&
			Objects.equals(id, that.id) &&
			Objects.equals(tableGroupId, that.tableGroupId);
	}

	@Override
	public int hashCode() {
		return Objects.hash(id, tableGroupId, numberOfGuests, empty);
	}
}
