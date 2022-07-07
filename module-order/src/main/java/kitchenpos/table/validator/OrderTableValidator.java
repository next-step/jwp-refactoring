package kitchenpos.table.validator;

import kitchenpos.order.domain.OrderRepository;
import kitchenpos.order.domain.OrderStatus;
import kitchenpos.table.domain.OrderTable;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.Objects;

import static kitchenpos.common.Messages.*;

@Component
public class OrderTableValidator {
    private final OrderRepository orderRepository;

    public OrderTableValidator(OrderRepository orderRepository) {
        this.orderRepository = orderRepository;
    }

    public void validateChangeEmpty(OrderTable orderTable) {
        validateHasOrderTable(orderTable);
        validateOrderStatus(orderTable);
    }

    private void validateHasOrderTable(OrderTable orderTable) {
        if (Objects.nonNull(orderTable.getTableGroupId())) {
            throw new IllegalArgumentException(HAS_ORDER_TABLE_GROUP);
        }
    }

    private void validateOrderStatus(OrderTable orderTable) {
        if (orderRepository.existsByOrderTableIdAndOrderStatusIn(
                orderTable.getTableGroupId(),
                Arrays.asList(OrderStatus.COOKING, OrderStatus.MEAL)
        )) {
            throw new IllegalArgumentException(ORDER_TABLE_STATUS_CANNOT_UPDATE);
        }
    }

    public void validateChangeNumberOfGuests(OrderTable orderTable) {
        if (orderTable.isEmpty()) {
            throw new IllegalArgumentException(ORDER_TABLE_CANNOT_EMPTY);
        }
    }
}
