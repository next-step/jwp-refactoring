package kitchenpos.dto;

public class OrderTableRequest_ChangeEmpty {
	private boolean empty;

	public OrderTableRequest_ChangeEmpty() {
	}

	public OrderTableRequest_ChangeEmpty(boolean empty) {
		this.empty = empty;
	}

	public boolean isEmpty() {
		return empty;
	}
}
