package kitchenpos.table.domain;

import java.util.List;
import java.util.stream.Stream;

import kitchenpos.table.exception.TableGroupException;

public class OrderTables {

	private static final int MIN_TABLE_GROUP_SIZE = 2;

	private List<OrderTable> orderTables;

	public OrderTables(List<OrderTable> orderTables) {
		validate(orderTables);
		this.orderTables = orderTables;
	}

	private void validate(List<OrderTable> orderTables) {
		if (orderTables.size() < MIN_TABLE_GROUP_SIZE) {
			throw new TableGroupException("단체 지정 시 2개 이상 주문테이블을 등록해야 합니다.");
		}

		if (orderTables.stream().anyMatch(orderTable -> !orderTable.isEmpty())) {
			throw new TableGroupException("단체 지정 시 주문 테이블이 비워져 있어야 합니다.");
		}
	}

	public void group(TableGroup tableGroup) {
		this.orderTables.forEach(orderTable -> {
			orderTable.group(tableGroup);
		});
	}

}
