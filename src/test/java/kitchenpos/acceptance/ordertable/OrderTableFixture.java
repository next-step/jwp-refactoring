package kitchenpos.acceptance.ordertable;

import kitchenpos.ui.dto.OrderTableRequest;
import kitchenpos.ui.dto.OrderTableResponse;

public class OrderTableFixture {

	public static OrderTableRequest 주문_테이블() {
		return new OrderTableRequest(0, false);
	}

	public static OrderTableResponse 주문_테이블(long id) {
		return new OrderTableResponse(id, 0, false);
	}

	public static OrderTableRequest 주문_불가_테이블(OrderTableResponse orderTableResponse) {
		return 주문_테이블(orderTableResponse.getNumberOfGuests(),
									  null);
	}

	public static OrderTableRequest 주문_테이블(int numberOfGuests) {
		return 주문_테이블(numberOfGuests, null);
	}

	public static OrderTableRequest 주문_테이블(Integer numberOfGuests, Boolean empty) {
		return new OrderTableRequest(numberOfGuests, empty);
	}
}
