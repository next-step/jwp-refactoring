package kitchenpos.table.event;

import kitchenpos.table.domain.OrderTableRepository;
import kitchenpos.table.domain.OrderTables;
import kitchenpos.tablegroup.event.GroupByEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Component
public class GroupByEventHandler {
    private final OrderTableRepository orderTableRepository;

    public GroupByEventHandler(OrderTableRepository orderTableRepository) {
        this.orderTableRepository = orderTableRepository;
    }

    @EventListener
    public void groupByEvent(GroupByEvent event) {
        final OrderTables savedOrderTables = OrderTables.of(
                orderTableRepository.findAllByIdIn(event.getOrderTableIds()));
        savedOrderTables.checkGroupable();
        savedOrderTables.groupBy(event.getTableGroupId());
    }
}
