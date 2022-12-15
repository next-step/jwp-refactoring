package kitchenpos.application;

import java.util.Objects;

import org.springframework.stereotype.Component;

import kitchenpos.domain.OrderTable;

@Component
public class TableValidator {
    public void validateChangeEmpty(OrderTable savedOrderTable, boolean orderStatusUnchangeable) {
        validateTableGroup(savedOrderTable);
        validateOrderStatus(orderStatusUnchangeable);
    }

    private void validateTableGroup(OrderTable savedOrderTable) {
        if (Objects.nonNull(savedOrderTable.getTableGroupId())) {
            throw new IllegalArgumentException("단체 지정이 되어있습니다.");
        }
    }

    private void validateOrderStatus(boolean cantChangeEmpty) {
        if (cantChangeEmpty) {
            throw new IllegalArgumentException("변경할 수 없는 주문 상태입니다.");
        }
    }
}
