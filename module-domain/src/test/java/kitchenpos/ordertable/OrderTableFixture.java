package kitchenpos.ordertable;

import kitchenpos.ordertable.domain.NumberOfGuests;
import kitchenpos.ordertable.domain.OrderTable;

public class OrderTableFixture {
	public static OrderTable 비어있지않은_주문_테이블_1번() {
		return OrderTable.of(1L, null, NumberOfGuests.from(4), false);
	}

	public static OrderTable 비어있지않은_주문_테이블_2번() {
		return OrderTable.of(2L, null, NumberOfGuests.from(4), false);
	}

	public static OrderTable 빈_주문_테이블_3번() {
		return OrderTable.of(3L, null, NumberOfGuests.from(0), true);
	}

	public static OrderTable 빈_주문_테이블_4번() {
		return OrderTable.of(4L, null, NumberOfGuests.from(0), true);
	}

	public static OrderTable 그룹핑된_주문_테이블_5번() {
		return OrderTable.of(5L, 1L, NumberOfGuests.from(0), true);
	}

	public static OrderTable 그룹핑된_주문_테이블_6번() {
		return OrderTable.of(6L, 1L, NumberOfGuests.from(0), true);
	}
}
