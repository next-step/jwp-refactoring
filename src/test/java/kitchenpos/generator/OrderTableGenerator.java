package kitchenpos.generator;

import kitchenpos.domain.OrderTable;

public class OrderTableGenerator {

	private OrderTableGenerator() {
	}

	public static OrderTable 주문테이블(Long tableGroupId, int numberOfGuests, boolean empty) {
		return new OrderTable(1L, tableGroupId, numberOfGuests, empty);
	}

	public static OrderTable 비어있지_않은_5명_테이블() {
		return 주문테이블(null, 5, false);
	}

	public static OrderTable 비어있는_테이블() {
		return 주문테이블(null, 0, true);
	}
}
