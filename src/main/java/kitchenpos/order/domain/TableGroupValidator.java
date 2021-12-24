package kitchenpos.order.domain;

import static kitchenpos.common.exception.ExceptionMessage.*;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import kitchenpos.common.exception.BadRequestException;
import kitchenpos.order.repository.OrderRepository;
import kitchenpos.order.repository.OrderTableRepository;

@Component
public class TableGroupValidator {

    private static final int ORDER_TABLE_MIN_SIZE = 2;

    private final OrderTableRepository orderTableRepository;
    private final OrderRepository orderRepository;

    public TableGroupValidator(OrderTableRepository orderTableRepository,
        OrderRepository orderRepository) {
        this.orderTableRepository = orderTableRepository;
        this.orderRepository = orderRepository;
    }

    public List<OrderTable> validateOrderTables(List<Long> orderTableIds) {
        final List<OrderTable> findOrderTables = orderTableRepository.findAllByIdIn(orderTableIds);
        validateExistOrderTables(orderTableIds, findOrderTables);
        validateSize(findOrderTables);
        validateOrderTable(findOrderTables);
        return findOrderTables;
    }

    private void validateExistOrderTables(List<Long> orderTableIds, List<OrderTable> orderTables) {
        if (!isExistOrderTables(orderTableIds, orderTables)) {
            throw new BadRequestException(WRONG_VALUE);
        }
    }

    private boolean isExistOrderTables(List<Long> orderTableIds, List<OrderTable> findOrderTables) {
        return orderTableIds.size() == findOrderTables.size();
    }

    private void validateSize(List<OrderTable> orderTables) {
        if (CollectionUtils.isEmpty(orderTables) || orderTables.size() < ORDER_TABLE_MIN_SIZE) {
            throw new BadRequestException(WRONG_VALUE);
        }
    }

    private void validateOrderTable(List<OrderTable> orderTables) {
        for (OrderTable orderTable : orderTables) {
            validatePossibleIntoTableGroup(orderTable);
        }
    }

    private void validatePossibleIntoTableGroup(OrderTable orderTable) {
        if (!orderTable.isPossibleIntoTableGroup()) {
            throw new BadRequestException(WRONG_VALUE);
        }
    }

    public void validateUngroup(Long tableGroupId) {
        List<OrderTable> findOrderTables = orderTableRepository.findAllByTableGroupId(tableGroupId);
        List<Long> orderTableIds = findOrderTables.stream()
            .map(OrderTable::getId).collect(Collectors.toList());

        if (existsNotCompletionOrderStatus(orderTableIds)) {
            throw new BadRequestException(CANNOT_CHANGE_STATUS);
        }
    }

    private boolean existsNotCompletionOrderStatus(List<Long> orderTableIds) {
        return orderRepository.existsByOrderTableIdInAndOrderStatusIn(orderTableIds,
            Arrays.asList(OrderStatus.COOKING, OrderStatus.MEAL));
    }
}
