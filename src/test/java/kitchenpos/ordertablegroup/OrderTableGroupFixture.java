package kitchenpos.ordertablegroup;

import static kitchenpos.ordertable.OrderTableFixture.*;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

import kitchenpos.domain.TableGroup;
import kitchenpos.ordertablegroup.dto.OrderTableGroupCreateRequest;

public class OrderTableGroupFixture {
	public static TableGroup 주문_테이블_그룹() {
		TableGroup tableGroup = new TableGroup();
		tableGroup.setId(1L);
		tableGroup.setCreatedDate(LocalDateTime.now());
		tableGroup.setOrderTables(Arrays.asList(빈_주문_테이블_1(), 빈_주문_테이블_2()));
		return tableGroup;
	}

	public static OrderTableGroupCreateRequest 주문_테이블_그룹_요청(List<Long> orderTableIds) {
		return new OrderTableGroupCreateRequest(orderTableIds);
	}
}
