package kitchenpos.table.domain;

import kitchenpos.order.domain.OrderRepository;
import kitchenpos.order.domain.OrderStatus;
import org.springframework.stereotype.Component;

import java.util.Objects;

@Component
public class TableValidator {
    private final OrderRepository orderRepository;

    public TableValidator(OrderRepository orderRepository) {
        this.orderRepository = orderRepository;
    }

    public void validateEmpty(OrderTable orderTable) {
        if (Objects.nonNull(orderTable.getTableGroup())) {
            throw new IllegalArgumentException("단체테이블로 지정된 주문테이블이 있습니다.");
        }
        if (isExistsTableAndOrderStatus(orderTable.getId())) {
            throw new IllegalArgumentException("주문테이블의 상태가 COOKING 또는 MEAL 입니다.");
        }
    }

    public void validateOrderTableEmpty(OrderTable orderTable) {
        if (orderTable.isEmpty()) {
            throw new IllegalArgumentException("주문 테이블이 비었습니다.");
        }
    }

    private boolean isExistsTableAndOrderStatus(Long orderTableId) {
        return orderRepository.existsByOrderTableIdAndOrderStatusIn(orderTableId, OrderStatus.getCookingAndMeal());
    }
}
