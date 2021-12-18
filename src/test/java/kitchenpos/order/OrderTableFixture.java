package kitchenpos.order;

import kitchenpos.order.dto.OrderTableRequest;

public class OrderTableFixture {
	public static OrderTableRequest 빈_주문_테이블() {
		return new OrderTableRequest(4, true);
	}

	public static OrderTableRequest 비어있지않은_주문_테이블() {
		return new OrderTableRequest(4, false);
	}
}
