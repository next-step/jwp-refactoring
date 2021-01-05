package kitchenpos.dto;

public class OrderTableRequest {

	private Long id;
	private int numberOfGuests;
	private boolean empty;

	private OrderTableRequest() {
	}

	private OrderTableRequest(Long id, boolean empty) {
		this.id = id;
		this.empty = empty;
	}

	public OrderTableRequest(Long id, int numberOfGuests) {
		this.id = id;
		this.numberOfGuests = numberOfGuests;
	}

	public static OrderTableRequest of(Long id, boolean empty) {
		return new OrderTableRequest(id, empty);
	}

	public static OrderTableRequest of(Long id, int numberOfGuests) {
		return new OrderTableRequest(id, numberOfGuests);
	}

	public Long getId() {
		return id;
	}

	public int getNumberOfGuests() {
		return numberOfGuests;
	}

	public boolean isEmpty() {
		return empty;
	}
}
