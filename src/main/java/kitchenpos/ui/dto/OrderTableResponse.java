package kitchenpos.ui.dto;

public class OrderTableResponse {

	private Long id;
	private Integer numberOfGuests;
	private Boolean empty;

	private OrderTableResponse() {
	}

	public OrderTableResponse(Long id, Integer numberOfGuests, Boolean empty) {
		this.id = id;
		this.numberOfGuests = numberOfGuests;
		this.empty = empty;
	}

	public Long getId() {
		return id;
	}

	public Integer getNumberOfGuests() {
		return numberOfGuests;
	}

	public Boolean getEmpty() {
		return empty;
	}
}
