package kitchenpos.table.acceptance.tablegroup;

import java.util.List;
import java.util.stream.Collectors;

import kitchenpos.table.ui.dto.OrderTableResponse;
import kitchenpos.table.ui.dto.TableGroupRequest;

public class TableGroupFixture {

	public static TableGroupRequest 주문_테이블_그룹(List<OrderTableResponse> orderTables) {
		return new TableGroupRequest(
			orderTables.stream()
				.map(OrderTableResponse::getId)
				.collect(Collectors.toList()));
	}
}
