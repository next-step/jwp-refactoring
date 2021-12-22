package kitchenpos.table.dto;

import kitchenpos.table.domain.OrderTable;

public class OrderTableRequest {

	private Integer numberOfGuest;
	private Boolean empty;

	protected OrderTableRequest() {
	}

	private OrderTableRequest(Integer numberOfGuest, Boolean empty) {
		this.numberOfGuest = numberOfGuest;
		this.empty = empty;
	}

	public static OrderTableRequest of(Integer numberOfGuest, Boolean empty) {
		return new OrderTableRequest(numberOfGuest, empty);
	}

	public Boolean getEmpty() {
		return empty;
	}

	public Integer getNumberOfGuest() {
		return numberOfGuest;
	}

	public OrderTable toEntity() {
		return OrderTable.of(numberOfGuest, empty);
	}
}
