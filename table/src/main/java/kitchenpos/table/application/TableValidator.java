package kitchenpos.table.application;

import java.util.Objects;

import org.springframework.stereotype.Component;

import kitchenpos.table.domain.OrderTable;

@Component
public class TableValidator {
    private final OrderSupport orderRepository;

    public TableValidator(OrderSupport orderSupport) {this.orderRepository = orderSupport;}

    public void validateChangeEmpty(OrderTable orderTable) {
        validateTableGroup(orderTable);
        validateOrderStatus(orderTable);
    }

    private void validateTableGroup(OrderTable orderTable) {
        if (Objects.nonNull(orderTable.getTableGroupId())) {
            throw new IllegalArgumentException("단체 지정이 되어있습니다.");
        }
    }

    private void validateOrderStatus(OrderTable orderTable) {
        if (orderRepository.validateOrderChangeable(orderTable.getId())) {
            throw new IllegalArgumentException("변경할 수 없는 주문 상태입니다.");
        }
    }
}
