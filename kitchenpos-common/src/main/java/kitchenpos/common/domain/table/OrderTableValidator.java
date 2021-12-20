package kitchenpos.common.domain.table;

import java.util.Arrays;
import java.util.List;

import javax.persistence.EntityNotFoundException;

import org.springframework.stereotype.Component;

import kitchenpos.common.domain.order.OrderRepository;
import kitchenpos.common.domain.order.OrderStatus;
import kitchenpos.common.exception.NotCompletionOrderException;
import kitchenpos.common.utils.StreamUtils;

@Component
public class OrderTableValidator {
    private static final String INVALID_GROUP_ORDER_TABLE_COUNT = "최소 2개 이상의 OrderTable 이 존재해야합니다.";
    private static final String INVALID_GROUP_ORDER_TABLE = "OrderTable 은 Grouping 시, TableGroup 이 할당되지 않으면서 비어있어야 합니다.";
    private static final String NOT_EXIST_ORDER_TABLE = "OrderTable 이 존재하지 않습니다.";
    private static final int MIN_ORDER_TABLE_COUNT = 2;

    private final OrderRepository orderRepository;

    public OrderTableValidator(OrderRepository orderRepository) {
        this.orderRepository = orderRepository;
    }

    public void validateGroupOrderTables(List<Long> orderTableIds, List<OrderTable> orderTables) {
        validateOrderTables(orderTableIds, orderTables);

        for (OrderTable orderTable : orderTables) {
            validateDoGroupOrderTable(orderTable);
        }
    }

    public void validateNotCompletionOrder(List<OrderTable> orderTables) {
        List<Long> orderTableIds = StreamUtils.mapToList(orderTables, OrderTable::getId);
        boolean isExistNotCompletion = orderRepository.existsByOrderTableIdInAndOrderStatusIn(orderTableIds,
                                                                                              Arrays.asList(
                                                                                                  OrderStatus.COOKING,
                                                                                                  OrderStatus.MEAL));
        if (isExistNotCompletion) {
            throw new NotCompletionOrderException();
        }
    }

    private void validateOrderTables(List<Long> orderTableIds, List<OrderTable> orderTables) {
        if (orderTableIds.size() < MIN_ORDER_TABLE_COUNT) {
            throw new IllegalArgumentException(INVALID_GROUP_ORDER_TABLE_COUNT);
        }

        if (orderTableIds.size() != orderTables.size()) {
            throw new EntityNotFoundException(NOT_EXIST_ORDER_TABLE);
        }
    }

    private static void validateDoGroupOrderTable(OrderTable orderTable) {
        if (!orderTable.isEmpty() || orderTable.hasTableGroup()) {
            throw new IllegalArgumentException(INVALID_GROUP_ORDER_TABLE);
        }
    }
}
