package kitchenpos.event;

import kitchenpos.domain.table.OrderTable;
import org.springframework.context.ApplicationEvent;

import java.util.List;

public class TableGroupCreatedEvent extends ApplicationEvent {

    private final List<OrderTable> orderTables;


    public TableGroupCreatedEvent(List<OrderTable> orderTables) {
        super(orderTables);
        this.orderTables = orderTables;
    }

    public List<OrderTable> getOrderTables() {
        return orderTables;
    }
}
