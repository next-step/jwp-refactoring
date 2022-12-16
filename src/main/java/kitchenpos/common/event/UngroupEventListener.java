package kitchenpos.common.event;

import kitchenpos.ordertable.repository.OrderTableRepository;
import kitchenpos.tablegroup.event.UnGroupEvent;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

@Component
public class UngroupEventListener {

    private final OrderTableRepository orderTableRepository;

    public UngroupEventListener(OrderTableRepository orderTableRepository) {
        this.orderTableRepository = orderTableRepository;
    }

    @Async
    @EventListener
    public void unGroup(UnGroupEvent unGroupEvent) {
        orderTableRepository.findListByTableGroupId(unGroupEvent
                .getTableGroupId())
                .ifPresent(orderTables -> orderTables
                        .forEach(orderTable -> orderTable
                                .changeTableGroupId(null)));
    }
}
