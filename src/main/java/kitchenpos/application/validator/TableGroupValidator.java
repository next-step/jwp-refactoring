package kitchenpos.application.validator;

import static kitchenpos.exception.ErrorCode.NOT_SAME_BETWEEN_ORDER_TABLES_COUNT_AND_SAVED_ORDER_TABLES;
import static kitchenpos.exception.ErrorCode.ORDER_TABLES_MUST_BE_AT_LEAST_TWO;
import static kitchenpos.exception.ErrorCode.TABLE_IS_NOT_EMPTY_OR_ALREADY_REGISTER_TABLE_GROUP;

import java.util.List;
import java.util.Objects;
import kitchenpos.domain.OrderTable;
import kitchenpos.exception.KitchenposException;
import org.springframework.util.CollectionUtils;

public class TableGroupValidator {
    public static void validateOrderTables(List<Long> orderTableIds, List<OrderTable> orderTables) {
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
}
