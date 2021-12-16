package kitchenpos.application.tablegroup;

import java.util.Arrays;
import java.util.List;

import javax.persistence.EntityNotFoundException;

import org.springframework.stereotype.Component;

import kitchenpos.domain.order.OrderRepository;
import kitchenpos.domain.order.OrderStatus;
import kitchenpos.domain.table.OrderTable;
import kitchenpos.domain.table.OrderTableRepository;
import kitchenpos.domain.tablegroup.TableGroup;
import kitchenpos.exception.NotCompletionOrderException;
import kitchenpos.utils.StreamUtils;

@Component
public class TableGroupValidator {
    private static final String INVALID_GROUP_ORDER_TABLE_COUNT = "최소 2개 이상의 OrderTable 이 존재해야합니다.";
    private static final String INVALID_GROUP_ORDER_TABLE = "OrderTable 은 TableGroup 이 할당되지 않으면서 비어있어야 합니다.";
    private static final String NOT_EXIST_ORDER_TABLE = "OrderTable 이 존재하지 않습니다.";
    private static final int MIN_ORDER_TABLE_COUNT = 2;

    private final OrderRepository orderRepository;
    private final OrderTableRepository orderTableRepository;

    public TableGroupValidator(OrderRepository orderRepository, OrderTableRepository orderTableRepository) {
        this.orderRepository = orderRepository;
        this.orderTableRepository = orderTableRepository;
    }

    public List<OrderTable> getValidGroupOrderTables(List<Long> orderTableIds) {
        List<OrderTable> orderTables = findOrderTables(orderTableIds);
        validateOrderTables(orderTableIds, orderTables);

        for (OrderTable orderTable : orderTables) {
            validateDoGroupOrderTable(orderTable);
        }

        return orderTables;
    }

    public void validateNotCompletionOrder(TableGroup tableGroup) {
        List<OrderTable> orderTables = orderTableRepository.findAllByTableGroupId(tableGroup.getId());
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

    private List<OrderTable> findOrderTables(List<Long> orderTableIds) {
        return orderTableRepository.findAllByIdIn(orderTableIds);
    }
}
