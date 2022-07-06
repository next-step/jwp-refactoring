package kitchenpos.table.domain;

import kitchenpos.tablegroup.event.TableGroupEventPublisher;
import kitchenpos.tablegroup.event.TableUnGroupEventPublisher;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.event.TransactionalEventListener;

import java.util.List;

import static kitchenpos.common.Messages.TABLE_GROUP_ORDER_IDS_FIND_IN_NO_SUCH;

@Component
@Transactional
public class OrderTableGroupHandler {

    private final OrderTableRepository orderTableRepository;

    public OrderTableGroupHandler(OrderTableRepository orderTableRepository) {
        this.orderTableRepository = orderTableRepository;
    }

    @TransactionalEventListener
    public void group(TableGroupEventPublisher tableGroupEventPublisher) {
        Long tableGroupId = tableGroupEventPublisher.getTableGroupId();
        List<Long> orderTableIds = tableGroupEventPublisher.getOrderTableIds();

        List<OrderTable> orderTables = orderTableRepository.findByIdIn(orderTableIds);
        validateOrderTable(orderTableIds, orderTables);

        orderTables.forEach(orderTable -> orderTable.setTableGroupId(tableGroupId));
    }

    private void validateOrderTable(List<Long> orderTableIds, List<OrderTable> orderTables) {
        if (orderTables.size() != orderTableIds.size()) {
            throw new IllegalArgumentException(TABLE_GROUP_ORDER_IDS_FIND_IN_NO_SUCH);
        }
    }

    @TransactionalEventListener
    public void unGroup(TableUnGroupEventPublisher tableUnGroupEventPublisher) {
        Long tableGroupId = tableUnGroupEventPublisher.getTableGroupId();

        List<OrderTable> orderTables = orderTableRepository.findAllByTableGroupId(tableGroupId);
        orderTables.forEach(OrderTable::ungroup);
    }
}
