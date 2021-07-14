package kitchenpos.table.event;

import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import kitchenpos.common.event.GroupedTablesEvent;
import kitchenpos.common.event.UngroupedTablesEvent;
import kitchenpos.table.domain.OrderTableRepository;
import kitchenpos.table.domain.OrderTables;

@Component
@Transactional
public class OrderTableEventHandler {

    private final OrderTableRepository orderTableRepository;

    public OrderTableEventHandler(OrderTableRepository orderTableRepository) {
        this.orderTableRepository = orderTableRepository;
    }

    @EventListener
    public void ungroupedTables(UngroupedTablesEvent ungroupedTablesEvent) {
        new OrderTables(orderTableRepository.findByIdIn(ungroupedTablesEvent.getOrderTables())).ungroupOrderTables();
    }

    @EventListener
    public void groupedTables(GroupedTablesEvent groupedTablesEvent) {
        new OrderTables(orderTableRepository.findByIdIn(groupedTablesEvent.getOrderTableIds()))
                .groupOrderTables(groupedTablesEvent.getTableGroupId());
    }
}
