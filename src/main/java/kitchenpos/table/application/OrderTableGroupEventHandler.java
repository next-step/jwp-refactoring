package kitchenpos.table.application;

import java.util.List;
import kitchenpos.order.event.TableGroupEvent;
import kitchenpos.table.domain.OrderTable;
import kitchenpos.table.domain.OrderTableRepository;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
public class OrderTableGroupEventHandler {

    private static final int MIN_ORDER_TABLE_COUNT = 2;

    private final OrderTableRepository orderTableRepository;

    public OrderTableGroupEventHandler(OrderTableRepository orderTableRepository) {
        this.orderTableRepository = orderTableRepository;
    }

    @Transactional
    @EventListener
    public void handle(TableGroupEvent event) {
        List<OrderTable> findOrderTables = orderTableRepository.findAllByIdIn(event.getOrderTableIds());
        validateSavedOrderTables(event.getOrderTableIds(), findOrderTables);
        validateAddingOrderTables(findOrderTables);
        findOrderTables.stream()
                .forEach(orderTable -> orderTable.attachToTableGroup(event.getTableGroupId()));
    }

    private void validateSavedOrderTables(List<Long> orderTableIds, List<OrderTable> orderTables) {
        if (orderTableIds.size() != orderTables.size()) {
            throw new IllegalArgumentException("시스템에 저장되지 않은 주문 테이블이 있습니다.");
        }
    }

    private void validateAddingOrderTables(List<OrderTable> orderTables) {
        if (orderTables.size() < MIN_ORDER_TABLE_COUNT) {
            throw new IllegalArgumentException("그룹 지정을 위해서는 " + MIN_ORDER_TABLE_COUNT + "개 이상의 주문 테이블이 필요합니다.");
        }

        if (checkOrderTableEmptyOrInTableGroup(orderTables)) {
            throw new IllegalArgumentException("단체 지정을 위해서는 그룹 지정이 되있지 않은 빈주문 테이블이 필요 합니다.");
        }
    }

    private boolean checkOrderTableEmptyOrInTableGroup(List<OrderTable> orderTables) {
        return orderTables.stream().anyMatch(orderTable -> !orderTable.isEmptyTable() || orderTable.isInTableGroup());
    }

}
