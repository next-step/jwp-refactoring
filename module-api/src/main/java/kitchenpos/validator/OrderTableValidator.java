package kitchenpos.validator;

import kitchenpos.exception.InvalidOrderStatusException;
import kitchenpos.exception.InvalidOrderTableException;
import kitchenpos.order.domain.OrderRepository;
import kitchenpos.order.domain.OrderStatus;
import kitchenpos.table.domain.OrderTable;
import kitchenpos.table.domain.OrderTables;
import org.springframework.stereotype.Component;

import java.util.Arrays;

@Component
public class OrderTableValidator {
    private final OrderRepository orderRepository;

    public OrderTableValidator(final OrderRepository orderRepository) {
        this.orderRepository = orderRepository;
    }

    public void checkTablesOrderStatus(final OrderTables orderTables) {
        if (orderRepository.existsByOrderTableIdInAndOrderStatusIn(
                orderTables.getTableIds(), Arrays.asList(OrderStatus.COOKING, OrderStatus.MEAL))) {
            throw new InvalidOrderTableException();
        }
    }

    public void checkTableOrderStatus(final OrderTable orderTable) {
        if (orderRepository.existsByOrderTableIdAndOrderStatusIn(
                orderTable.getId(), Arrays.asList(OrderStatus.COOKING, OrderStatus.MEAL))) {
            throw new InvalidOrderStatusException();
        }
    }
}
