package kitchenpos.table.dto;

import java.util.List;
import java.util.stream.Collectors;

import kitchenpos.table.domain.OrderTable;
import kitchenpos.table.domain.OrderTables;

public class OrderTableResponse {
	private Long id;
	private Long tableGroup;
	private Integer numberOfGuests;
	private Boolean empty;

	protected OrderTableResponse() {
	}

	private OrderTableResponse(Long id, Long tableGroup, Integer numberOfGuests, Boolean empty) {
		this.id = id;
		this.tableGroup = tableGroup;
		this.numberOfGuests = numberOfGuests;
		this.empty = empty;
	}

	public static OrderTableResponse from(OrderTable orderTable) {
		return new OrderTableResponse(orderTable.getId(), orderTable.getTableGroupId(), orderTable.getNumberOfGuests(),
			orderTable.isEmpty());
	}

	public static OrderTableResponse of(Long id, Long tableGroup, Integer numberOfGuests,
		Boolean empty) {
		return new OrderTableResponse(id, tableGroup, numberOfGuests, empty);
	}

	public static List<OrderTableResponse> ofList(OrderTables orderTables) {
		return orderTables.getOrderTables()
			.stream()
			.map(OrderTableResponse::from)
			.collect(Collectors.toList());
	}

	public static List<OrderTableResponse> ofList(List<OrderTable> orderTables) {
		return orderTables.stream()
			.map(OrderTableResponse::from)
			.collect(Collectors.toList());
	}

	public Long getId() {
		return id;
	}

	public Long getTableGroup() {
		return tableGroup;
	}

	public Integer getNumberOfGuests() {
		return numberOfGuests;
	}

	public Boolean getEmpty() {
		return empty;
	}

}
