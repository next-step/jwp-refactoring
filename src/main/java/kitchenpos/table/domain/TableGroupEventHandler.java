package kitchenpos.table.domain;

import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Component
@Transactional
public class TableGroupEventHandler {
    @Async
    @EventListener
    public void tableGroupingHandle(TableGroupEvent tableGroupEvent) {
        List<OrderTable> orderTables = tableGroupEvent.getOrderTables();
        orderTables.forEach(orderTable -> orderTable.addGroup(tableGroupEvent.getTableGroupId()));
    }

    @Async
    @EventListener
    public void tableUngroupingHandle(TableUngroupEvent tableUngroupEvent) {
        List<OrderTable> orderTables = tableUngroupEvent.getOrderTables();
        orderTables.forEach(OrderTable::removeGroup);
    }
}
