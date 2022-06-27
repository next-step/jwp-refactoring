package kitchenpos.tableGroup.domain;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import kitchenpos.core.exception.BadRequestException;
import kitchenpos.core.exception.CannotCreateException;
import kitchenpos.core.exception.CannotUpdateException;
import kitchenpos.core.exception.ExceptionType;
import kitchenpos.order.domain.OrderRepository;
import kitchenpos.order.domain.OrderStatus;
import kitchenpos.table.domain.OrderTable;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

@Component
public class TableGroupValidator {
    private final OrderRepository orderRepository;

    public TableGroupValidator(OrderRepository orderRepository) {
        this.orderRepository = orderRepository;
    }

    public void validate(List<Long> orderTableIds, List<OrderTable> orderTables) {
        validateOrderTableSize(orderTableIds);
        validateOrderTableEqualsSize(orderTables, orderTableIds);
        validateTablesEmpty(orderTables);
    }

    public void validateOrderTablesStatus(TableGroup tableGroup) {
        List<Long> orderTableIds = tableGroup.getOrderTables().stream()
            .map(OrderTable::getId)
            .collect(Collectors.toList());

        if (orderRepository.existsByOrderTableIdInAndOrderStatusIn(
            orderTableIds, Arrays.asList(OrderStatus.COOKING, OrderStatus.MEAL))) {
            throw new CannotUpdateException(ExceptionType.CAN_NOT_UPDATE_TABLE_IN_COOKING_AND_MEAL_STATUS);
        }
    }

    private void validateOrderTableSize(List<Long> orderTableIds) {
        if (CollectionUtils.isEmpty(orderTableIds) || orderTableIds.size() < 2) {
            throw new CannotCreateException(ExceptionType.ORDER_TABLE_AT_LEAST_TWO);
        }
    }

    private void validateTablesEmpty(List<OrderTable> items) {
        items.stream()
            .filter(it -> !it.isEmpty() || it.isGrouped())
            .findFirst()
            .ifPresent(e -> {
                throw new CannotCreateException(ExceptionType.MUST_NOT_BE_EMPTY_OR_GROUPED_TABLE);
            });
    }

    private void validateOrderTableEqualsSize(List<OrderTable> savedOrderTables, List<Long> orderTableIds)  {
        if (savedOrderTables.size() != orderTableIds.size()) {
            throw new BadRequestException(ExceptionType.CONTAINS_NOT_EXIST_ORDER_TABLE);
        }
    }
}
