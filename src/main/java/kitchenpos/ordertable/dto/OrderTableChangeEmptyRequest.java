package kitchenpos.ordertable.dto;

public class OrderTableChangeEmptyRequest {

	private boolean isEmpty;

	public OrderTableChangeEmptyRequest(boolean isEmpty) {
		this.isEmpty = isEmpty;
	}

	public boolean isEmpty() {
		return isEmpty;
	}
}
