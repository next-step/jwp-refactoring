package kitchenpos.table.domain;

import kitchenpos.table.dto.OrderTableIdRequest;
import org.springframework.util.CollectionUtils;

import java.util.List;
import java.util.Objects;

public class TableGroupValidator {

    public static void validate(List<OrderTableIdRequest> orderTables, List<OrderTable> savedOrderTables) {
        validateParam(orderTables);
        validateExist(orderTables, savedOrderTables);
        validateOrderTable(savedOrderTables);
    }

    public static void validateParam(List<OrderTableIdRequest> orderTables) {
        validateEmptyOrderTables(orderTables);
        validateSizeOrderTables(orderTables);
    }

    public static void validateExist(List<OrderTableIdRequest> orderTables, List<OrderTable> savedOrderTables) {
        if (orderTables.size() != savedOrderTables.size()) {
            throw new IllegalArgumentException();
        }
    }

    public static void validateOrderTable(List<OrderTable> savedOrderTables) {
        for (OrderTable savedOrderTable : savedOrderTables) {
            validateEmptyOrderTable(savedOrderTable);
            validateIncludeTableGroup(savedOrderTable);
        }
    }

    public static void validateEmptyOrderTables(List<OrderTableIdRequest> orderTables) {
        if (CollectionUtils.isEmpty(orderTables)) {
            throw new IllegalArgumentException();
        }
    }

    public static void validateSizeOrderTables(List<OrderTableIdRequest> orderTables) {
        if (orderTables.size() < 2) {
            throw new IllegalArgumentException();
        }
    }

    public static void validateEmptyOrderTable(OrderTable savedOrderTable) {
        if (!savedOrderTable.isEmpty()) {
            throw new IllegalArgumentException();
        }
    }

    public static void validateIncludeTableGroup(OrderTable savedOrderTable) {
        if (Objects.nonNull(savedOrderTable.getTableGroupId())) {
            throw new IllegalArgumentException();
        }
    }

}
