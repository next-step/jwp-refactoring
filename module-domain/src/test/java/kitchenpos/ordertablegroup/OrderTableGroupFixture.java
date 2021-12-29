package kitchenpos.ordertablegroup;

import java.util.List;

import kitchenpos.ordertablegroup.domain.OrderTableGroup;
import kitchenpos.ordertablegroup.domain.ValidOrderTableGroupValidator;

public class OrderTableGroupFixture {
	public static OrderTableGroup 주문_테이블_그룹(List<Long> orderTableIds) {
		OrderTableGroup orderTableGroup = OrderTableGroup.from(1L);
		orderTableGroup.group(orderTableIds, new ValidOrderTableGroupValidator());
		return orderTableGroup;
	}
}
