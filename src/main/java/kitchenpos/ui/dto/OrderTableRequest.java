package kitchenpos.ui.dto;

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
}
