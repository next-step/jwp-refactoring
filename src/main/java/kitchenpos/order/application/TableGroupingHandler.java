package kitchenpos.order.application;

import kitchenpos.order.domain.OrderTable;
import kitchenpos.order.domain.TableRepository;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class TableGroupingHandler {
    private final TableRepository tableRepository;

    public TableGroupingHandler(final TableRepository tableRepository) {
        this.tableRepository = tableRepository;
    }

    @EventListener
    public void handle(TableGroupingEvent event) {
        List<OrderTable> orderTables = tableRepository.findAllById(event.getTableIds());
        orderTables.forEach(orderTable -> orderTable.group(event.getTableGroupId()));
    }

    @EventListener
    public void handle(TableUnGroupingEvent event) {
        List<OrderTable> orderTables = tableRepository.findAllById(event.getTableIds());
        orderTables.forEach(OrderTable::unGroup);
    }
}
