package kitchenpos.event;

import kitchenpos.domain.OrderTable;
import org.springframework.context.ApplicationEvent;

import java.util.List;

public class TableGroupUnGroupEvent extends ApplicationEvent {

    private final List<OrderTable> orderTables;

    public TableGroupUnGroupEvent(List<OrderTable> orderTables) {
        super(orderTables);
        this.orderTables = orderTables;
    }

    public List<OrderTable> getOrderTables() {
        return orderTables;
    }
}
