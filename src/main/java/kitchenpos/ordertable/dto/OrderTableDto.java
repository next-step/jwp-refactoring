package kitchenpos.ordertable.dto;

public class OrderTableDto {
	private Long id;
	private Long tableGroupId;
	private int numberOfGuests;
	private boolean empty;

	public OrderTableDto() {
	}

	public OrderTableDto(Long id, Long tableGroupId, int numberOfGuests, boolean empty) {
		this.id = id;
		this.tableGroupId = tableGroupId;
		this.numberOfGuests = numberOfGuests;
		this.empty = empty;
	}

	public Long getId() {
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
