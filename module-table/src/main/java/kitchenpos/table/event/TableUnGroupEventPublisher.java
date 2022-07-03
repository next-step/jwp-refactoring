package kitchenpos.table.event;

import java.util.List;
import kitchenpos.table.domain.OrderTable;

public class TableUnGroupEventPublisher {
    private final List<OrderTable> orderTables;

    public TableUnGroupEventPublisher(List<OrderTable> orderTables) {
        this.orderTables = orderTables;
    }

    public List<OrderTable> getOrderTables() {
        return orderTables;
    }
}
