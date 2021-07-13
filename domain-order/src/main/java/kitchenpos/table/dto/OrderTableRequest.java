package kitchenpos.table.dto;

import kitchenpos.table.domain.OrderTable;

public class OrderTableRequest {
	private Long id;

	private int numberOfGuests;

	private boolean empty;

	public OrderTableRequest() {}

	public OrderTableRequest(Long id, int numberOfGuests, boolean empty) {
		this.id = id;
		this.numberOfGuests = numberOfGuests;
		this.empty = empty;
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

	public OrderTable toEntity() {
		return new OrderTable(numberOfGuests, empty);
	}
}
