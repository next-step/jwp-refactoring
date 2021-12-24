package kitchenpos.order.domain;

import static kitchenpos.common.exception.ExceptionMessage.*;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Component;

import kitchenpos.common.exception.BadRequestException;
import kitchenpos.order.repository.OrderRepository;
import kitchenpos.order.repository.OrderTableRepository;

@Component
public class TableGroupValidator {

    private final OrderTableRepository orderTableRepository;
    private final OrderRepository orderRepository;

    public TableGroupValidator(OrderTableRepository orderTableRepository,
        OrderRepository orderRepository) {
        this.orderTableRepository = orderTableRepository;
        this.orderRepository = orderRepository;
    }

    public List<OrderTable> validateExistOrderTable(List<Long> orderTableIds) {
        final List<OrderTable> findOrderTables = orderTableRepository.findAllByIdIn(orderTableIds);
        if (!isExistOrderTables(orderTableIds, findOrderTables)) {
            throw new BadRequestException(WRONG_VALUE);
        }
        return findOrderTables;
    }

    private boolean isExistOrderTables(List<Long> orderTableIds, List<OrderTable> findOrderTables) {
        return orderTableIds.size() == findOrderTables.size();
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
