package kitchenpos.ordertable;

import kitchenpos.ordertable.domain.NumberOfGuests;
import kitchenpos.ordertable.domain.OrderTable;
import kitchenpos.ordertable.dto.OrderTableRequest;

public class OrderTableFixture {
	public static OrderTable 비어있지않은_주문_테이블_1번() {
		return OrderTable.of(1L, NumberOfGuests.from(4), false);
	}

	public static OrderTable 빈_주문_테이블_2번() {
		return OrderTable.of(2L, NumberOfGuests.from(0), true);
	}

	public static OrderTable 주문_테이블_그룹에_속한_주문_테이블_3번() {
		return OrderTable.of(3L, NumberOfGuests.from(0), true);
	}

	public static OrderTableRequest 빈_주문_테이블_요청() {
		return new OrderTableRequest(4, true);
	}

	public static OrderTableRequest 비어있지않은_주문_테이블_요청() {
		return new OrderTableRequest(4, false);
	}

	public static OrderTableRequest 비우기_요청() {
		return new OrderTableRequest(true);
	}

	public static OrderTableRequest 채우기_요청() {
		return new OrderTableRequest(false);
	}

	public static OrderTableRequest 손님_수_변경_요청(int numberOfGuests) {
		return new OrderTableRequest(numberOfGuests);
	}
}
