package kitchenpos.table.domain.fixture;

import kitchenpos.table.domain.OrderTables;

import java.util.Collections;

import static kitchenpos.table.domain.fixture.OrderTableFixture.notEmptyOrderTable;

public class OrderTablesFixture {

    public static OrderTables orderTablesA() {
        OrderTables orderTables = new OrderTables();
        orderTables.addAll(Collections.singletonList(notEmptyOrderTable()));
        return new OrderTables();
    }
}
