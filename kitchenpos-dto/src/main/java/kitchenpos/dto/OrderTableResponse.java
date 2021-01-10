package kitchenpos.dto;

import kitchenpos.domain.OrderTable;
import kitchenpos.exception.NotFoundException;

public class OrderTableResponse {

	private Long id;
	private int numberOfGuests;
	private boolean empty;

	private OrderTableResponse(Long id, int numberOfGuests, boolean empty) {
		this.id = id;
		this.numberOfGuests = numberOfGuests;
		this.empty = empty;
	}

	public static OrderTableResponse of(OrderTable orderTable) {
		if (orderTable == null) {
			throw new NotFoundException("주문 테이블 정보를 찾을 수 없습니다.");
		}
		return new OrderTableResponse(orderTable.getId(), orderTable.getNumberOfGuests(), orderTable.isEmpty());
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
