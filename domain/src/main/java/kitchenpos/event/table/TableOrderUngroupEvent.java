package kitchenpos.event.table;

import kitchenpos.domain.table.OrderTable;
import org.springframework.context.ApplicationEvent;

import java.util.List;

public class TableOrderUngroupEvent extends ApplicationEvent {

    private final List<OrderTable> orderTables;

    public TableOrderUngroupEvent(List<OrderTable> orderTables) {
        super(orderTables);
        this.orderTables = orderTables;
    }

    public List<OrderTable> getOrderTables() {
        return orderTables;
    }
}
