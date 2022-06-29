package kitchenpos.table.application;

import kitchenpos.order.domain.OrderRepository;
import kitchenpos.order.domain.OrderStatus;
import kitchenpos.table.domain.OrderTables;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

@Component
public class TableValidator {
    private final OrderRepository orderRepository;

    public TableValidator(OrderRepository orderRepository) {
        this.orderRepository = orderRepository;
    }

    public void validateOrderTableNotCompletion(Long orderTableId) {
        if (isExistsOrderTablesAndNotCompletion(Collections.singletonList(orderTableId))) {
            throw new IllegalArgumentException();
        }
    }

    public void validateOrderTablesNotCompletion(OrderTables orderTables) {
        if (isExistsOrderTablesAndNotCompletion(orderTables.findOrderTableIds())) {
            throw new IllegalArgumentException();
        }
    }

    private boolean isExistsOrderTablesAndNotCompletion(List<Long> orderTableIds) {
        return orderRepository.existsByOrderTableIdInAndOrderStatusIn(
                orderTableIds, Arrays.asList(OrderStatus.COOKING.name(), OrderStatus.MEAL.name()));
    }

}
