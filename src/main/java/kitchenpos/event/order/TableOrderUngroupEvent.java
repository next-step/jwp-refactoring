package kitchenpos.event.order;

import kitchenpos.domain.order.OrderTable;
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
