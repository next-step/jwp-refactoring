package kitchenpos.table.application;

import java.util.Arrays;
import java.util.Objects;

import org.springframework.stereotype.Component;

import kitchenpos.order.domain.OrderRepository;
import kitchenpos.order.domain.OrderStatus;
import kitchenpos.table.domain.OrderTable;

@Component
public class TableValidator {
    private final OrderRepository orderRepository;

    public TableValidator(OrderRepository orderRepository) {this.orderRepository = orderRepository;}

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
        if (orderRepository.existsByOrderTableIdAndOrderStatusIn(orderTable.getId(),
            Arrays.asList(OrderStatus.COOKING, OrderStatus.MEAL))) {
            throw new IllegalArgumentException("변경할 수 없는 주문 상태입니다.");
        }
    }
}
