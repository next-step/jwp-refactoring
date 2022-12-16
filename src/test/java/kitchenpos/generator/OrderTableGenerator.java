package kitchenpos.generator;

import kitchenpos.domain.OrderTable;

public class OrderTableGenerator {

	private OrderTableGenerator() {
	}

	public static OrderTable 주문테이블(Long tableGroupId, int numberOfGuests, boolean empty) {
		return new OrderTable(1L, tableGroupId, numberOfGuests, empty);
	}
}
