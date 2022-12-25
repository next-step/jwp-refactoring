package kitchenpos.ordertable.listener;

import java.util.List;
import kitchenpos.ordertable.repository.OrderTableRepository;
import kitchenpos.ordertable.domain.OrderTable;
import kitchenpos.table.event.TableGroupCreateEvent;
import kitchenpos.table.event.TableUnGroupEvent;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
public class OrderTableEventListener {

    private final OrderTableRepository orderTableRepository;

    public OrderTableEventListener(OrderTableRepository orderTableRepository) {
        this.orderTableRepository = orderTableRepository;
    }

    @Async
    @EventListener
    @Transactional
    public void group(TableGroupCreateEvent event) {
        List<OrderTable> orderTables = orderTableRepository.findAllById(event.getOrderTableIds());
        orderTables.forEach(orderTable -> orderTable.setTableGroupId(event.getTableGroupId()));
    }

    @Async
    @EventListener
    @Transactional
    public void unGroup(TableUnGroupEvent event) {
        List<OrderTable> orderTables = orderTableRepository.findAllByTableGroupId(event.getTableGroupId());
        orderTables.forEach(OrderTable::ungroup);
    }
}
