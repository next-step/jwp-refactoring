package kitchenpos.ordertable.event.handler;

import kitchenpos.ordertable.domain.OrderTable;
import kitchenpos.ordertable.domain.OrderTableRepository;
import kitchenpos.ordertable.event.GroupTableEvent;
import kitchenpos.ordertable.exception.IllegalOrderTableException;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Component
public class GroupTableEventHandler {
    private final OrderTableRepository orderTableRepository;

    public static final String ERROR_ORDER_TABLE_NOT_EMPTY = "주문테이블은 비어있어야 합니다.";
    public static final String ERROR_ORDER_TABLE_INVALID = "주문테이블이 올바르지 않습니다.";
    public static final String ERROR_ORDER_TABLE_GROUPED = "주문테이블은 그룹에 지정되어있을 수 없습니다.";
    public static final String ERROR_ORDER_TABLE_TOO_SMALL = "주문테이블의 개수는 %d 미만일 수 없습니다.";
    public static final int MIN_ORDER_TABLE_NUMBER = 2;

    public GroupTableEventHandler(OrderTableRepository orderTableRepository) {
        this.orderTableRepository = orderTableRepository;
    }

    @Transactional
    @EventListener
    public void handle(GroupTableEvent event) {
        List<Long> orderTableIds = event.getOrderTableIds();
        List<OrderTable> orderTables = orderTableRepository.findAllByIdIn(orderTableIds);
        validateOrderTablesToGroup(orderTableIds, orderTables);

        orderTables.forEach(orderTable -> orderTable.assignTableGroup(event.getTableGroupId()));
    }

    private void validateOrderTablesToGroup(List<Long> orderTableIds, List<OrderTable> orderTables) {
        validateOrderTablesSize(orderTables);
        validateOrderTablesValid(orderTableIds, orderTables);
        validateOrderTablesNotGrouped(orderTables);
        validateOrderTablesEmpty(orderTables);
    }

    private void validateOrderTablesSize(List<OrderTable> orderTables) {
        if (orderTables == null || orderTables.size() < MIN_ORDER_TABLE_NUMBER) {
            throw new IllegalOrderTableException(
                    String.format(ERROR_ORDER_TABLE_TOO_SMALL, MIN_ORDER_TABLE_NUMBER)
            );
        }
    }

    private void validateOrderTablesValid(List<Long> orderTableIds, List<OrderTable> orderTables) {
        if(orderTableIds.size() != orderTables.size()){
            throw new IllegalOrderTableException(ERROR_ORDER_TABLE_INVALID);
        }
    }

    private void validateOrderTablesNotGrouped(List<OrderTable> orderTables) {
        if (orderTables.stream().
                anyMatch(orderTable -> orderTable.isGrouped())) {
            throw new IllegalOrderTableException(ERROR_ORDER_TABLE_GROUPED);
        }
    }

    private void validateOrderTablesEmpty(List<OrderTable> orderTables) {
        if (orderTables.stream().
                anyMatch(orderTable -> !orderTable.isEmpty())) {
            throw new IllegalOrderTableException(ERROR_ORDER_TABLE_NOT_EMPTY);
        }
    }
}
