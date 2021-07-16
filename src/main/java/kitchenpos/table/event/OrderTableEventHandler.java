package kitchenpos.table.event;

import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import kitchenpos.common.event.GroupedTablesEvent;
import kitchenpos.common.event.UngroupedTablesEvent;
import kitchenpos.table.domain.OrderTableRepository;
import kitchenpos.table.domain.OrderTables;

@Component
public class OrderTableEventHandler {

    private final OrderTableRepository orderTableRepository;

    public OrderTableEventHandler(OrderTableRepository orderTableRepository) {
        this.orderTableRepository = orderTableRepository;
    }

    @Transactional
    @EventListener
    public void ungroupedTables(UngroupedTablesEvent ungroupedTablesEvent) {
        new OrderTables(orderTableRepository.findByIdIn(ungroupedTablesEvent.getOrderTables())).ungroupOrderTables();
    }

    @Transactional
    @EventListener
    public void groupedTables(GroupedTablesEvent groupedTablesEvent) {
        new OrderTables(orderTableRepository.findByIdIn(groupedTablesEvent.getOrderTableIds()))
                .groupOrderTables(groupedTablesEvent.getTableGroupId());
    }
}
