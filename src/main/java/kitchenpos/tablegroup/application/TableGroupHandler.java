package kitchenpos.tablegroup.application;

import kitchenpos.table.domain.OrderTable;
import kitchenpos.table.domain.OrderTableRepository;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.event.TransactionalEventListener;

import java.util.List;

@Transactional(readOnly = true)
@Component
public class TableGroupHandler {

    private final OrderTableRepository orderTableRepository;

    public TableGroupHandler(OrderTableRepository orderTableRepository) {
        this.orderTableRepository = orderTableRepository;
    }

    @Async
    @TransactionalEventListener
    @Transactional
    public void group(TableGroupEvent event) {
        Long tableGroupId = event.getTableGroupId();
        List<Long> orderTableIds = event.getOrderTableIds();

        List<OrderTable> orderTables = orderTableRepository.findAllByIdIn(orderTableIds);
        if (orderTables.size() != orderTableIds.size()) {
            throw new IllegalArgumentException("그룹화할 테이블 수가 일치하지 않습니다.");
        }
        orderTables.forEach(orderTable -> orderTable.group(tableGroupId));
    }

    @Async
    @TransactionalEventListener
    @Transactional
    public void ungroup(TableUngroupEvent event) {
        Long tableGroupId = event.getTableGroupId();

        List<OrderTable> orderTables = orderTableRepository.findAllByTableGroupId(tableGroupId);
        orderTables.forEach(OrderTable::ungroup);
    }
}
