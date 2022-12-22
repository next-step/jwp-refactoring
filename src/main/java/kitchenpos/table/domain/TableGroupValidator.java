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
            throw new IllegalArgumentException("존재하는 주문 테이블로만 구성되어 있어야 합니다.");
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
            throw new IllegalArgumentException("주문 테이블은 요청 필수값 입니다.");
        }
    }

    public static void validateSizeOrderTables(List<OrderTableIdRequest> orderTables) {
        if (orderTables.size() < 2) {
            throw new IllegalArgumentException("주문 테이블은 2개 이상이어야 합니다.");
        }
    }

    public static void validateEmptyOrderTable(OrderTable savedOrderTable) {
        if (!savedOrderTable.isEmpty()) {
            throw new IllegalArgumentException("주문 테이블은 비어있지 않아야 합니다.");
        }
    }

    public static void validateIncludeTableGroup(OrderTable savedOrderTable) {
        if (Objects.nonNull(savedOrderTable.getTableGroupId())) {
            throw new IllegalArgumentException("주문 테이블은 항상 테이블 그룹에 속해있어야 합니다.");
        }
    }

}
