package kitchenpos.ordertable.dto;

import java.util.List;
import java.util.stream.Collectors;

import kitchenpos.ordertable.domain.OrderTable;

public class OrderTableResponse {

	private Long id;
	private boolean isEmpty;
	private Long tableGroupId;
	private int numberOfGuests;

	public OrderTableResponse(Long id, boolean isEmpty, int numberOfGuests) {
		this.id = id;
		this.isEmpty = isEmpty;
		this.tableGroupId = null;
		this.numberOfGuests = numberOfGuests;
	}

	public OrderTableResponse(Long id, boolean isEmpty, Long tableGroupId, int numberOfGuests) {
		this.id = id;
		this.isEmpty = isEmpty;
		this.tableGroupId = tableGroupId;
		this.numberOfGuests = numberOfGuests;
	}

	public Long getId() {
		return id;
	}

	public boolean isEmpty() {
		return isEmpty;
	}

	public Long getTableGroupId() {
		return tableGroupId;
	}

	public int getNumberOfGuests() {
		return numberOfGuests;
	}

	public static OrderTableResponse of(OrderTable orderTable) {
		return new OrderTableResponse(orderTable.getId(), orderTable.isEmpty(), 1L,
			orderTable.getNumberOfGuests().value());
	}

	public static List<OrderTableResponse> of(List<OrderTable> orderTables) {
		return orderTables.stream()
			.map(OrderTableResponse::of)
			.collect(Collectors.toList());
	}

}
