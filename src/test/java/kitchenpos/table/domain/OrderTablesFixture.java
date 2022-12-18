package kitchenpos.table.domain;

import java.util.Collections;

public class OrderTablesFixture {

    public static OrderTables orderTablesA() {
        OrderTables orderTables = new OrderTables();
        orderTables.addAll(Collections.singletonList(OrderTableFixture.orderTableA()));
        return new OrderTables();
    }
}
