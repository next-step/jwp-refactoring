package kitchenpos.order;

import java.util.List;

import kitchenpos.order.dto.OrderTableGroupCreateRequest;

public class OrderTableGroupFixture {
	public static OrderTableGroupCreateRequest 주문_테이블_그룹(List<Long> orderTableIds) {
		return new OrderTableGroupCreateRequest(orderTableIds);
	}
}
