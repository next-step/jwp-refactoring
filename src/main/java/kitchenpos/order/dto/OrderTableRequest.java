package kitchenpos.order.dto;

import kitchenpos.order.domain.OrderTable;

public class OrderTableRequest {
	private Long id;
	private int numberOfGuests;
	private boolean empty;

	public OrderTableRequest() {
	}

	public OrderTableRequest(Long id) {
		this.id = id;
	}

	public OrderTableRequest(int numberOfGuests) {
		this.numberOfGuests = numberOfGuests;
	}

	public OrderTableRequest(boolean empty) {
		this.empty = empty;
	}

	public OrderTableRequest(Integer numberOfGuests, Boolean empty) {
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
		return new OrderTable(this.id, this.numberOfGuests, this.empty);
	}
}
