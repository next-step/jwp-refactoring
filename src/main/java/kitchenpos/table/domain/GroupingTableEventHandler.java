package kitchenpos.table.domain;

import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import kitchenpos.tablegroup.domain.GroupingTableEvent;

@Component
public class GroupingTableEventHandler {
    private final OrderTableRepository orderTableRepository;

    public GroupingTableEventHandler(OrderTableRepository orderTableRepository) {
        this.orderTableRepository = orderTableRepository;
    }

    @Transactional
    @EventListener
    public void handle(GroupingTableEvent event) {
        OrderTables orderTables = new OrderTables(orderTableRepository.findAllByIdIn(event.getOrderTableIds()));

        orderTables.group(event);
        orderTableRepository.saveAll(orderTables.getOrderTables());
    }
}
