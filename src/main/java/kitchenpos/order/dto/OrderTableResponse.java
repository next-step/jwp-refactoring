package kitchenpos.order.dto;

import kitchenpos.common.BaseIdEntity;
import kitchenpos.order.domain.OrderTable;

import java.util.Optional;

public class OrderTableResponse {
	private long id;
	private Long tableGroupId;
	private int numberOfGuests;
	private boolean empty;

	public OrderTableResponse() {
	}

	public static OrderTableResponse of(OrderTable orderTable) {
		Long tableGroupId = Optional.ofNullable(orderTable.getTableGroup())
				.map(BaseIdEntity::getId)
				.orElse(null);
		return new OrderTableResponse(orderTable.getId(),
				tableGroupId,
				orderTable.getNumberOfGuests().getValue(),
				orderTable.isEmpty());
	}

	public OrderTableResponse(long id, Long tableGroupId, int numberOfGuests, boolean empty) {
		this.id = id;
		this.tableGroupId = tableGroupId;
		this.numberOfGuests = numberOfGuests;
		this.empty = empty;
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
