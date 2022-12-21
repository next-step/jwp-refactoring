package kitchenpos.table.validator;

import static kitchenpos.exception.ErrorCode.EXISTS_NOT_COMPLETION_STATUS;
import static kitchenpos.exception.ErrorCode.NOT_SAME_BETWEEN_ORDER_TABLES_COUNT_AND_SAVED_ORDER_TABLES;
import static kitchenpos.exception.ErrorCode.ORDER_TABLES_MUST_BE_AT_LEAST_TWO;
import static kitchenpos.exception.ErrorCode.TABLE_IS_NOT_EMPTY_OR_ALREADY_REGISTER_TABLE_GROUP;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import kitchenpos.order.domain.OrderStatus;
import kitchenpos.table.domain.OrderTable;
import kitchenpos.exception.KitchenposException;
import kitchenpos.order.domain.OrderRepository;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

@Component
public class TableGroupValidator {
    private final OrderRepository orderRepository;

    public TableGroupValidator(OrderRepository orderRepository) {
        this.orderRepository = orderRepository;
    }

    public void validateCreate(List<Long> orderTableIds, List<OrderTable> orderTables) {
        validateOrderTablesSize(orderTables);
        validateOrderTablesEmpty(orderTables);
        validateOrderTablesSize(orderTableIds, orderTables);
    }

    private static void validateOrderTablesSize(List<OrderTable> orderTables){
        if (isOrderTablesEmpty(orderTables) || isLessThanTwo(orderTables)) {
            throw new KitchenposException(ORDER_TABLES_MUST_BE_AT_LEAST_TWO);
        }
    }

    private static boolean isOrderTablesEmpty(List<OrderTable> orderTables){
        return CollectionUtils.isEmpty(orderTables);
    }

    private static boolean isLessThanTwo(List<OrderTable> orderTables){
        return orderTables.size() < 2;
    }

    private static void validateOrderTablesEmpty(List<OrderTable> orderTables) {
        for (final OrderTable orderTable : orderTables) {
            if (!orderTable.isEmpty() || Objects.nonNull(orderTable.getTableGroup())) {
                throw new KitchenposException(TABLE_IS_NOT_EMPTY_OR_ALREADY_REGISTER_TABLE_GROUP);
            }
        }
    }

    private static void validateOrderTablesSize(List<Long> orderTableIds, List<OrderTable> savedOrderTables) {
        if (orderTableIds.size() != savedOrderTables.size()) {
            throw new KitchenposException(NOT_SAME_BETWEEN_ORDER_TABLES_COUNT_AND_SAVED_ORDER_TABLES);
        }
    }

    public void existsByCookingAndMeal(List<Long> orderTableIds){
        if (orderRepository.existsByOrderTableIdInAndOrderStatusIn(
                orderTableIds, Arrays.asList(OrderStatus.COOKING, OrderStatus.MEAL))) {
            throw new KitchenposException(EXISTS_NOT_COMPLETION_STATUS);
        }
    }
}
