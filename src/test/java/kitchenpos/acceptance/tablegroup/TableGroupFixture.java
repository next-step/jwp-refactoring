package kitchenpos.acceptance.tablegroup;

import java.util.List;
import java.util.stream.Collectors;

import kitchenpos.ui.dto.OrderTableResponse;
import kitchenpos.ui.dto.TableGroupRequest;

public class TableGroupFixture {

	public static TableGroupRequest 주문_테이블_그룹(List<OrderTableResponse> orderTables) {
		return new TableGroupRequest(
			orderTables.stream()
				.map(OrderTableResponse::getId)
				.collect(Collectors.toList()));
	}
}
