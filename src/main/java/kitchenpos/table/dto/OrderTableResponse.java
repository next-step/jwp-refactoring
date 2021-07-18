package kitchenpos.table.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;

import kitchenpos.table.domain.NumberOfGuests;
import kitchenpos.table.domain.OrderTable;
import kitchenpos.tableGroup.domain.TableGroup;

public class OrderTableResponse {
	private Long id;
	@JsonIgnore
	private TableGroup tableGroup;
	private NumberOfGuests numberOfGuests;
	private boolean empty;

	public OrderTableResponse() {
	}

	public OrderTableResponse(Long id, TableGroup tableGroup, NumberOfGuests numberOfGuests, boolean empty) {
		this.id = id;
		this.tableGroup = tableGroup;
		this.numberOfGuests = numberOfGuests;
		this.empty = empty;
	}

	public Long getId() {
		return id;
	}

	public TableGroup getTableGroup() {
		return tableGroup;
	}

	public NumberOfGuests getNumberOfGuests() {
		return numberOfGuests;
	}

	public boolean isEmpty() {
		return empty;
	}

	public static OrderTableResponse of(OrderTable orderTable) {
		return new OrderTableResponse(orderTable.getId(), orderTable.getTableGroup(), orderTable.getNumberOfGuests(), orderTable.isEmpty());
	}
}
