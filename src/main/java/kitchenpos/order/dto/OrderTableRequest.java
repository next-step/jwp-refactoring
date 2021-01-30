package kitchenpos.order.dto;

import kitchenpos.order.domain.OrderTable;

public class OrderTableRequest {
	private Long id;
	private int GuestsNumber;
	private boolean empty;

	protected OrderTableRequest() {
	}

	public OrderTableRequest(Long id) {
		this.id = id;
	}

	public OrderTableRequest(int GuestsNumber, boolean empty) {
		this.GuestsNumber = GuestsNumber;
		this.empty = empty;
	}

	public OrderTableRequest(int GuestsNumber) {
		this.GuestsNumber = GuestsNumber;
	}

	public OrderTableRequest(boolean empty) {
		this.empty = empty;
	}

	public Long getId() {
		return id;
	}

	public int getGuestsNumber() {
		return GuestsNumber;
	}

	public boolean isEmpty() {
		return empty;
	}

	public OrderTable toOrderTable() {
		return OrderTable.builder()
			.numberOfGuests(GuestsNumber)
			.empty(empty)
			.build();
	}
}
