package kitchenpos.ordertablegroup;

import java.util.List;

import kitchenpos.ordertablegroup.dto.OrderTableGroupCreateRequest;

public class OrderTableGroupFixture {
	public static OrderTableGroupCreateRequest 주문_테이블_그룹_요청(List<Long> orderTableIds) {
		return new OrderTableGroupCreateRequest(orderTableIds);
	}
}
