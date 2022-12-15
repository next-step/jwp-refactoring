package kitchenpos.order.event;

import kitchenpos.order.domain.OrderTable;
import kitchenpos.order.repository.OrderTableRepository;
import kitchenpos.tablegroup.event.TableGroupEventPublisher;
import kitchenpos.tablegroup.event.TableUnGroupEventPublisher;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionalEventListener;

import javax.transaction.Transactional;
import java.util.List;

@Component
@Transactional
public class OrderTableGroupEventHandler {
    private final OrderTableRepository orderTableRepository;

    public OrderTableGroupEventHandler(OrderTableRepository orderTableRepository) {
        this.orderTableRepository = orderTableRepository;
    }

    @TransactionalEventListener
    public void group(TableGroupEventPublisher tableGroupEventPublisher) {
        Long tableGroupId = tableGroupEventPublisher.getTableGroupId();
        List<Long> orderTableIds = tableGroupEventPublisher.getOrderTableIds();

        List<OrderTable> orderTables = orderTableRepository.findAllById(orderTableIds);
        orderTables.forEach(orderTable -> orderTable.setTableGroupId(tableGroupId));
    }

    @TransactionalEventListener
    public void unGroup(TableUnGroupEventPublisher tableUnGroupEventPublisher) {
        Long tableGroupId = tableUnGroupEventPublisher.getTableGroupId();

        List<OrderTable> orderTables = orderTableRepository.findAllByTableGroupId(tableGroupId);
        orderTables.forEach(OrderTable::ungroup);
    }
}
