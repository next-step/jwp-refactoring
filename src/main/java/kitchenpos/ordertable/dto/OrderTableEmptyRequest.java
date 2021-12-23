package kitchenpos.ordertable.dto;

public class OrderTableEmptyRequest {

	private boolean empty;

	protected OrderTableEmptyRequest() {
	}

	private OrderTableEmptyRequest(boolean empty) {
		this.empty = empty;
	}

	public static OrderTableEmptyRequest of(boolean empty) {
		return new OrderTableEmptyRequest(empty);
	}

	public boolean isEmpty() {
		return empty;
	}
}
