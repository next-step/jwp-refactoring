package kitchenpos.table.application;

import kitchenpos.order.domain.OrderRepository;
import kitchenpos.order.domain.OrderStatus;
import kitchenpos.order.exception.OrderStatusException;
import kitchenpos.order.exception.OrderTableEmptyException;
import kitchenpos.table.domain.OrderTable;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;

@Component
public class TableValidator {

    private final OrderRepository orderRepository;

    public TableValidator(OrderRepository orderRepository) {
        this.orderRepository = orderRepository;
    }

    public void validateChangeEmpty(OrderTable orderTable) {
        validateExistsTableGroup(orderTable);
        validateOrderStatus(orderTable);
    }

    public void validateChangeNumberOfGuests(OrderTable orderTable) {
        if (orderTable.getEmpty().isEmpty()) {
            throw new OrderTableEmptyException();
        }
    }

    private void validateExistsTableGroup(OrderTable orderTable) {
        if (Objects.nonNull(orderTable.getTableGroupId())) {
            throw new IllegalArgumentException("이미 속해있는 테이블 그룹이 있습니다.");
        }
    }

    private void validateOrderStatus(OrderTable orderTable) {
        List<OrderStatus> orderStatuses = Arrays.asList(OrderStatus.COOKING, OrderStatus.MEAL);
        if (orderRepository.existsByOrderTableIdAndOrderStatusIn(orderTable.getId(), orderStatuses)) {
            throw new OrderStatusException();
        }
    }
}
