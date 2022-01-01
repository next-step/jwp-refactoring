package kitchenpos.order.eventHandler;

import kitchenpos.order.domain.OrderTableRepository;
import kitchenpos.order.domain.OrderTables;
import kitchenpos.tableGroup.event.GroupEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Component
public class GroupEventHandler {
    private final OrderTableRepository orderTableRepository;

    public GroupEventHandler(OrderTableRepository orderTableRepository) {
        this.orderTableRepository = orderTableRepository;
    }

    @EventListener
    public void onGroupEvent(GroupEvent event) {
        final OrderTables orderTables = OrderTables.of(event.getOrderTableIds().size(), orderTableRepository.findAllById(event.getOrderTableIds()));
        orderTables.group(event.getTableGroupId());
    }
}
