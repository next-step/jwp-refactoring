package kitchenpos.table.dto;

public class TableEmptyUpdateRequest {

	private boolean empty;

	public TableEmptyUpdateRequest() {
	}

	public TableEmptyUpdateRequest(boolean empty) {
		this.empty = empty;
	}

	public boolean isEmpty() {
		return empty;
	}
	
}
