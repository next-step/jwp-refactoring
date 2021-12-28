package kitchenpos.table.domain;

import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Component
@Transactional
public class TableGroupEventHandler {
    private final OrderTableRepository orderTableRepository;

    public TableGroupEventHandler(final OrderTableRepository orderTableRepository) {
        this.orderTableRepository = orderTableRepository;
    }

    @Async
    @EventListener
    public void tableGroupingHandle(TableGroupEvent tableGroupEvent) {
        List<Long> orderTableIds = tableGroupEvent.getOrderTableIds();

        List<OrderTable> orderTables = orderTableRepository.findAllById(orderTableIds);
        orderTables.forEach(orderTable -> orderTable.addGroup(tableGroupEvent.getTableGroupId()));
    }

    @Async
    @EventListener
    public void tableUngroupingHandle(TableUngroupEvent tableUngroupEvent) {
        List<OrderTable> orderTables = orderTableRepository.findOrderTableByTableGroupId(tableUngroupEvent.getTableGroupId());

        orderTables.forEach(OrderTable::removeGroup);

        System.out.println("orderTables = " + orderTables);
        orderTableRepository.saveAll(orderTables);
    }
}
