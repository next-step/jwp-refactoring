package kitchenpos.table.application.validator;

import static kitchenpos.common.exception.ErrorCode.NOT_SAME_BETWEEN_ORDER_TABLES_COUNT_AND_SAVED_ORDER_TABLES;
import static kitchenpos.common.exception.ErrorCode.ORDER_TABLES_MUST_BE_AT_LEAST_TWO;
import static kitchenpos.common.exception.ErrorCode.TABLE_IS_NOT_EMPTY_OR_ALREADY_REGISTER_TABLE_GROUP;

import java.util.List;
import java.util.Objects;
import kitchenpos.common.exception.KitchenposException;
import kitchenpos.table.domain.OrderTable;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

@Component
public class TableGroupValidator {
    private final OrderStatusValidator orderStatusValidator;

    public TableGroupValidator(OrderStatusValidator orderStatusValidator) {
        this.orderStatusValidator = orderStatusValidator;
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
        orderStatusValidator.existsByOrderTableIdInAndOrderStatusIn(orderTableIds);
    }
}
