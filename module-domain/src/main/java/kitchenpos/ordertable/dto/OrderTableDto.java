package kitchenpos.ordertable.dto;

import kitchenpos.ordertable.domain.OrderTable;

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

	public static OrderTableDto from(OrderTable orderTable) {
		OrderTableDto dto = new OrderTableDto();
		dto.id = orderTable.getId();
		dto.tableGroupId = orderTable.getOrderTableGroupId();
		dto.numberOfGuests = orderTable.getNumberOfGuests().getValue();
		dto.empty = orderTable.isEmpty();
		return dto;
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
