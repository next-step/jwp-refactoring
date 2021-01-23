package kitchenpos.dto.order;


import kitchenpos.domain.order.OrderTable;

import java.util.List;
import java.util.stream.Collectors;

public class OrderTableResponse {
	private Long id;
	private Long tableGroupId;
	private int numberOfGuests;
	private boolean empty;

	public OrderTableResponse(Long id, Long tableGroupId, int numberOfGuests, boolean empty) {
		this.id = id;
		this.tableGroupId = tableGroupId;
		this.numberOfGuests = numberOfGuests;
		this.empty = empty;
	}


	public static List<OrderTableResponse> of(List<OrderTable> orderTable) {
		return orderTable.stream().map(OrderTableResponse::of).collect(Collectors.toList());
	}

	public static OrderTableResponse of(OrderTable orderTable) {
		return new OrderTableResponse(orderTable.getId(), orderTable.getTableGroupId(), orderTable.getNumberOfGuests(),
				orderTable.isEmpty());
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Long getTableGroupId() {
		return tableGroupId;
	}

	public void setTableGroupId(Long tableGroupId) {
		this.tableGroupId = tableGroupId;
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
