package kitchenpos.ordertable.domain;

import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import kitchenpos.tablegroup.domain.TableGroupedEvent;
import kitchenpos.tablegroup.domain.TableUngroupedEvent;

@Component
public class TableGroupingEventHandler {
    private final OrderTableRepository orderTableRepository;

    public TableGroupingEventHandler(OrderTableRepository orderTableRepository) {
        this.orderTableRepository = orderTableRepository;
    }

    @EventListener
    @Transactional
    public void handle(TableGroupedEvent event) {
        orderTableRepository.findAllById(event.getOrderTableIds())
            .forEach(orderTable -> orderTable.groupedBy(event.getTableGroupId()));
    }

    @EventListener
    @Transactional
    public void handle(TableUngroupedEvent event) {
        orderTableRepository.findAllByTableGroupId(event.getTableGroupId())
            .forEach(OrderTable::leaveTableGroup);
    }
}
