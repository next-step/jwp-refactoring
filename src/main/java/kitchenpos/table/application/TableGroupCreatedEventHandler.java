package kitchenpos.table.application;

import java.util.List;

import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import kitchenpos.table.domain.OrderTableRepository;
import kitchenpos.table.domain.OrderTables;
import kitchenpos.tablegroup.dto.TableGroupCreatedEvent;

@Component
public class TableGroupCreatedEventHandler {
    private static final int MIN_ORDER_TABLE_SIZE = 2;

    private final OrderTableRepository orderTableRepository;

    public TableGroupCreatedEventHandler(OrderTableRepository orderTableRepository) {
        this.orderTableRepository = orderTableRepository;
    }

    @EventListener
    @Transactional
    public void handle(final TableGroupCreatedEvent event) {
        final List<Long> orderTableIds = event.getOrderTableIds();
        final Long tableGroupId = event.getTableGroupId();
        final OrderTables savedOrderTables = findOrderTables(orderTableIds);

        validate(orderTableIds, savedOrderTables);
        savedOrderTables.group(tableGroupId);
    }

    private OrderTables findOrderTables(List<Long> orderTableIds) {
        return OrderTables.from(orderTableRepository.findAllByIdIn(orderTableIds));
    }

    private void validate(final List<Long> orderTableIds, final OrderTables savedOrderTables) {
        if (orderTableIds.size() < MIN_ORDER_TABLE_SIZE) {
            throw new IllegalArgumentException("최소 2개 이상의 주문테이블 ID를 입력하셔야 합니다.");
        }
        if (!savedOrderTables.hasSize(orderTableIds.size())) {
            throw new IllegalArgumentException("등록되어있지 않는 주문 테이블 ID를 입력하셨습니다.");
        }
        if (savedOrderTables.hasAnyNotEmpty()) {
            throw new IllegalArgumentException("모든 주문 테이블은 빈 테이블 상태이어야 합니다.");
        }
        if (savedOrderTables.hasAnyTableGroupRegistered()) {
            throw new IllegalArgumentException("이미 단체 지정되어있는 테이블이 존재합니다.");
        }
    }
}
