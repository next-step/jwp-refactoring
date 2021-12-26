package kitchenpos.order.event;

import kitchenpos.order.application.TableService;
import kitchenpos.order.domain.OrderTable;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class OrderTableEventHandler {
    private final TableService tableService;

    public OrderTableEventHandler(TableService tableService) {
        this.tableService = tableService;
    }

    @EventListener
    public void grouped(OrderTableGrouped event) {
        List<OrderTable> orderTables = event.getOrderTables();

        tableService.grouped(event.getTableGroupId(), orderTables);
    }

    @EventListener
    public void ungrouped(OrderTableUngrouped event) {
        tableService.ungrouped(event.getTableGroupId());
    }

}
