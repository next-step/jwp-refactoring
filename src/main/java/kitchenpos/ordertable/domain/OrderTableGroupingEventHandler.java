package kitchenpos.ordertable.domain;

import java.util.List;
import kitchenpos.tablegroup.domain.TableGroupingEvent;
import kitchenpos.tablegroup.domain.TableUnGroupingEvent;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
public class OrderTableGroupingEventHandler {

    private final OrderTableRepository orderTableRepository;

    public OrderTableGroupingEventHandler(final OrderTableRepository orderTableRepository) {
        this.orderTableRepository = orderTableRepository;
    }

    @Async
    @EventListener
    @Transactional
    public void groupingHandle(TableGroupingEvent event) {
        List<OrderTable> orderTables = orderTableRepository.findAllById(event.getOrderTableIds());
        orderTables.forEach(orderTable -> orderTable.group(event.getTableGroupId()));

        orderTableRepository.saveAll(orderTables);
    }

    @Async
    @EventListener
    @Transactional
    public void unGroupingHandle(TableUnGroupingEvent event) {
        List<OrderTable> orderTables = orderTableRepository.findAllByTableGroupId(
            event.getTableGroupId());

        orderTables.forEach(OrderTable::ungroup);
    }
}
