package kitchenpos.table.application;

import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

import kitchenpos.table.domain.OrderTableRepository;
import kitchenpos.table.domain.OrderTables;
import kitchenpos.tablegroup.dto.TableGroupUngroupedEvent;

@Component
public class TableGroupUngroupedEventHandler {
    private final OrderSupport orderSupport;
    private final OrderTableRepository orderTableRepository;

    public TableGroupUngroupedEventHandler(OrderSupport orderSupport, OrderTableRepository orderTableRepository) {
        this.orderSupport = orderSupport;
        this.orderTableRepository = orderTableRepository;
    }

    @TransactionalEventListener(phase = TransactionPhase.BEFORE_COMMIT)
    public void handle(final TableGroupUngroupedEvent event) {
        final Long tableGroupId = event.getTableGroupId();
        final OrderTables orderTables = findOrderTables(tableGroupId);

        validate(orderTables);
        orderTables.ungroup();
    }

    private OrderTables findOrderTables(Long tableGroupId) {
        return OrderTables.from(orderTableRepository.findAllByTableGroupId(tableGroupId));
    }

    private void validate(OrderTables orderTables) {
        if (orderSupport.validateOrderChangeable(orderTables.toIds())) {
            throw new IllegalArgumentException("변경할 수 없는 주문 상태입니다.");
        }
    }
}
