package kitchenpos.ui.dto;

import kitchenpos.domain.OrderTable2;

public class OrderTableRequest {

	private Integer numberOfGuests;
	private Boolean empty;

	private OrderTableRequest() {
	}

	public OrderTableRequest(Integer numberOfGuests, Boolean empty) {
		this.numberOfGuests = numberOfGuests;
		this.empty = empty;
	}

	public Integer getNumberOfGuests() {
		return numberOfGuests;
	}

	public Boolean getEmpty() {
		return empty;
	}

	public OrderTable2 toOrderTable() {
		return new OrderTable2(numberOfGuests, empty);
	}

}
