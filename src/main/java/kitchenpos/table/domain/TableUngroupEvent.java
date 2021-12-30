package kitchenpos.table.domain;

import java.util.List;

public class TableUngroupEvent {
    private final List<OrderTable> orderTables;


    private TableUngroupEvent(List<OrderTable> orderTables) {
        this.orderTables = orderTables;
    }

    public static TableUngroupEvent of(List<OrderTable> orderTables) {
        return new TableUngroupEvent(orderTables);
    }

    public List<OrderTable> getOrderTables() {
        return orderTables;
    }
}
