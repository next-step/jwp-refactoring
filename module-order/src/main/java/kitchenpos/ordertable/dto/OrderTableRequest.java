package kitchenpos.ordertable.dto;

import kitchenpos.ordertable.domain.OrderTable;

public class OrderTableRequest {

	private Long id;
	private int numberOfGuests;
	private boolean empty;

	public OrderTableRequest() {
	}

	public OrderTableRequest(long id) {
		this.id = id;
	}

	public OrderTableRequest(boolean empty) {
		this(null, 0, empty);
	}

	public OrderTableRequest(int numberOfGuests) {
		this(null, numberOfGuests, false);
	}

	public OrderTableRequest(int numberOfGuests, boolean empty) {
		this(null, numberOfGuests, empty);
	}

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

	public OrderTable toOrderTable() {
		return new OrderTable(numberOfGuests, empty);
	}
}

