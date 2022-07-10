package kitchenpos.order.generator;

import kitchenpos.table.domain.OrderTables;
import kitchenpos.tableGroup.domain.TableGroup;

public class TableGroupGenerator {

    public static TableGroup 테이블_그룹_생성(OrderTables orderTables) {
        return new TableGroup(orderTables);
    }
}
