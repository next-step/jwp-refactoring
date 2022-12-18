package kitchenpos.order.ui.request;

public class TableStatusRequest {

	private final boolean empty;

	public TableStatusRequest(boolean empty) {
		this.empty = empty;
	}

	public boolean isEmpty() {
		return empty;
	}
}
