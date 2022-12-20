package kitchenpos.generator;

import java.util.List;

import kitchenpos.order.domain.OrderTable;
import kitchenpos.order.domain.TableGroup;

public class TableGroupGenerator {

	private TableGroupGenerator() {
	}

	public static TableGroup 단체_지정(List<OrderTable> orderTables) {
		return TableGroup.from(orderTables);
	}
}
