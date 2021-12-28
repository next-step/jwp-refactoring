package kitchenpos.table.domain;

import kitchenpos.common.exception.BadRequestException;
import kitchenpos.order.domain.Order;
import kitchenpos.order.domain.OrderRepository;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Component
public class TableGroupValidator {
    private static final int MINIMUM_GROUP_SIZE = 2;

    private final OrderRepository orderRepository;
    private final OrderTableRepository orderTableRepository;

    public TableGroupValidator(OrderRepository orderRepository, OrderTableRepository orderTableRepository) {
        this.orderRepository = orderRepository;
        this.orderTableRepository = orderTableRepository;
    }

    public void validateGrouping(List<Long> orderTableIds) {
        checkOrderTableSizeValidation(orderTableIds);
        checkOrderTableGroupIsEmpty(orderTableIds);
        checkIsAbleToGrouping(orderTableIds);
    }

    public void validateUngrouping(Long tableGroupId) {
        checkGroupOrderIsNotComplete(tableGroupId);
    }

    private void checkGroupOrderIsNotComplete(Long tableGroupId) {
        List<Long> orderTableIds = orderTableRepository.findOrderTableByTableGroupId(tableGroupId)
                .stream()
                .map(OrderTable::getId)
                .collect(Collectors.toList());
        orderTableIds.forEach(this::checkOrderIsNotComplete);
    }

    private void checkOrderIsNotComplete(Long orderTableId) {
        List<Order> orders = orderRepository.findOrderByOrderTableId(orderTableId);
        boolean isComplete = orders.stream()
                .allMatch(Order::isComplete);

        if (!isComplete) {
            throw new BadRequestException("완료되지 않은 주문이 존재하여 테이블 그룹을 해제할 수 없습니다.");
        }
    }

    private void checkOrderTableGroupIsEmpty(List<Long> orderTableIds) {
        List<OrderTable> orderTables = orderTableRepository.findAllById(orderTableIds);

        boolean isGroupEmpty = orderTables.stream()
                .allMatch(orderTable -> Objects.isNull(orderTable.getTableGroupId()));

        if (!isGroupEmpty) {
            throw new BadRequestException("이미 그룹이 존재하는 주문 테이블입니다.");
        }
    }

    private void checkIsAbleToGrouping(List<Long> orderTables) {
        orderTables.forEach(this::checkCompletedOrderTable);
    }

    private void checkCompletedOrderTable(Long orderTableId) {
        List<Order> orders = orderRepository.findOrderByOrderTableId(orderTableId);

        boolean isComplete = orders.stream()
                .allMatch(Order::isComplete);

        if (!isComplete) {
            throw new BadRequestException("완료되지 않은 주문이 존재합니다.");
        }
    }

    private void checkOrderTableSizeValidation(List<Long> orderTables) {
        if (orderTables.isEmpty()) {
            throw new BadRequestException("주문 테이블이 존재하지 않아 그룹화할 수 없습니다.");
        }

        if (orderTables.size() < MINIMUM_GROUP_SIZE) {
            throw new BadRequestException("주문 테이블 " + MINIMUM_GROUP_SIZE + "개 이상 그룹화할 수 있습니다.");
        }
    }
}
