package kitchenpos.dto.order;


import kitchenpos.domain.order.OrderTable;

public class OrderTableRequest {
	private int numberOfGuests;
	private boolean empty;

	public OrderTableRequest() {
		this.numberOfGuests = 0;
		this.empty = true;
	}
	public OrderTableRequest(int numberOfGuests) {
		this.numberOfGuests = numberOfGuests;
	}

	public OrderTableRequest(int numberOfGuests, boolean empty) {
		this.numberOfGuests = numberOfGuests;
		this.empty = empty;
	}

	public OrderTable toEntity() {
		return new OrderTable(0, true);
	}

	public int getNumberOfGuests() {
		return numberOfGuests;
	}

	public void setNumberOfGuests(int numberOfGuests) {
		this.numberOfGuests = numberOfGuests;
	}

	public boolean isEmpty() {
		return empty;
	}

	public void setEmpty(boolean empty) {
		this.empty = empty;
	}
}
