package kitchenpos.table.domain;

import org.springframework.util.CollectionUtils;

import java.util.List;
import java.util.Objects;

public class TableGroupCreateValidator {

    private static int MIN = 2;

    public static void validate(List<Long> orderTableIds, List<OrderTable> orderTables) {
        validateOrderTablesSameSize(orderTableIds, orderTables);
        validateOrderTableMinSize(orderTables);
        validateOrderTableIsEmptyOrNull(orderTables);
    }

    private static void validateOrderTablesSameSize(List<Long> orderTableIds, List<OrderTable> orderTables) {
        if (orderTableIds.size() != orderTables.size()) {
            throw new IllegalArgumentException("주문 테이블의 중복 되었거나, 주문 테이블이 존재하지 않습니다.");
        }
    }

    public static void validateOrderTableMinSize(List<OrderTable> orderTables) {
        if (CollectionUtils.isEmpty(orderTables) || orderTables.size() < MIN) {
            throw new IllegalArgumentException("주문 테이블이 존재하지 않거나 부족합니다.");
        }
    }

    private static void validateOrderTableIsEmptyOrNull(List<OrderTable> orderTables) {
        for (final OrderTable orderTable : orderTables) {
            if (!orderTable.isEmpty() || Objects.nonNull(orderTable.getTableGroupId())) {
                throw new IllegalArgumentException("주문 테이블이 비어있지 않거나, 이미 단체에 소속되어 있습니다.");
            }
        }
    }

}
