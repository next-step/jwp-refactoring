package kitchenpos.event;

import java.util.List;
import kitchenpos.ordertable.domain.OrderTable;
import kitchenpos.ordertable.repository.OrderTableRepository;
import kitchenpos.tablegroup.event.TableGroupedEvent;
import kitchenpos.tablegroup.event.TableUnGroupedEvent;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

@Component
public class TableGroupEventListener {

    private final OrderTableRepository orderTableRepository;

    public TableGroupEventListener(OrderTableRepository orderTableRepository) {
        this.orderTableRepository = orderTableRepository;
    }

    @TransactionalEventListener(phase = TransactionPhase.BEFORE_COMMIT)
    public void onGroupedEvent(TableGroupedEvent tableGroupedEvent) {
        List<OrderTable> orderTables = orderTableRepository.findAllByIdIn(tableGroupedEvent.getOrderTableIds())
                .orElseThrow(() -> new IllegalArgumentException("등록 된 주문 테이블에 대해서만 단체 지정이 가능합니다"));
        orderTables.forEach(orderTable -> {
            orderTable.changeEmpty(false);
            orderTable.changeTableGroupId(tableGroupedEvent.getTableGroupId());
        });
    }

    @TransactionalEventListener(phase = TransactionPhase.BEFORE_COMMIT)
    public void onUnGroupedEvent(TableUnGroupedEvent tableUnGroupedEvent) {
        orderTableRepository.findListByTableGroupId(tableUnGroupedEvent
                .getTableGroupId())
                .ifPresent(orderTables -> orderTables
                        .forEach(orderTable -> orderTable
                                .changeTableGroupId(null)));
    }
}
