package kitchenpos.order.ui.response;

public class OrderTableResponse {

	private long id;
	private Long tableGroupId;
	private int numberOfGuests;
	private boolean empty;

	private OrderTableResponse() {
	}

	private OrderTableResponse(long id, Long tableGroupId, int numberOfGuests, boolean empty) {
		this.id = id;
		this.tableGroupId = tableGroupId;
		this.numberOfGuests = numberOfGuests;
		this.empty = empty;

	}

	public static OrderTableResponse of(long id, Long tableGroupId, int numberOfGuests, boolean empty) {
		return new OrderTableResponse(id, tableGroupId, numberOfGuests, empty);
	}

	public long getId() {
		return id;
	}

	public Long getTableGroupId() {
		return tableGroupId;
	}

	public int getNumberOfGuests() {
		return numberOfGuests;
	}

	public boolean isEmpty() {
		return empty;
	}
}
