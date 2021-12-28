package kitchenpos.ordertable.domain;

import kitchenpos.ordertable.infra.OrderTableRepository;
import kitchenpos.tablegroup.domain.TableGroupedEvent;
import kitchenpos.tablegroup.domain.TableUnGroupedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Component
public class OrderTableGroupingEventHandler {

    private final OrderTableRepository orderTableRepository;

    public OrderTableGroupingEventHandler(final OrderTableRepository orderTableRepository) {
        this.orderTableRepository = orderTableRepository;
    }

    @Async
    @EventListener
    @Transactional
    public void groupingHandle(TableGroupedEvent event) {
        final List<OrderTable> orderTables = orderTableRepository.findAllById(event.getOrderTableIds());
        final Long tableGroupId = event.getTableGroupId();
        orderTables.forEach(orderTable -> orderTable.group(tableGroupId));
//        orderTableRepository.saveAll(orderTables);
    }

    @Async
    @EventListener
    @Transactional(readOnly = true)
    public void unGroupingHandle(TableUnGroupedEvent event) {
        List<OrderTable> orderTables = orderTableRepository.findAllByTableGroupId(event.getTableGroupId());
        orderTables.forEach(OrderTable::ungroup);
    }
}
