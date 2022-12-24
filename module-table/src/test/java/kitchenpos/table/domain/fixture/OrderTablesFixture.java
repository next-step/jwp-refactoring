package kitchenpos.table.domain.fixture;

import kitchenpos.table.domain.OrderTables;

import java.util.Collections;

public class OrderTablesFixture {

    public static OrderTables orderTablesA() {
        return new OrderTables(Collections.singletonList(OrderTableFixture.notEmptyOrderTable()));
    }
}
