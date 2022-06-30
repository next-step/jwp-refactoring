package kitchenpos.order.application;

import kitchenpos.order.domain.OrderRepository;
import kitchenpos.order.domain.OrderStatus;
import kitchenpos.table.application.StatusValidator;
import kitchenpos.table.domain.OrderTables;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

@Component
public class OrderStatusValidator implements StatusValidator {
    private final OrderRepository orderRepository;

    public OrderStatusValidator(OrderRepository orderRepository) {
        this.orderRepository = orderRepository;
    }

    @Override
    public void validateOrderTableNotCompletion(Long orderTableId) {
        if (isExistsOrderTablesAndNotCompletion(Collections.singletonList(orderTableId))) {
            throw new IllegalArgumentException();
        }
    }

    @Override
    public void validateOrderTablesNotCompletion(OrderTables orderTables) {
        if (isExistsOrderTablesAndNotCompletion(orderTables.findOrderTableIds())) {
            throw new IllegalArgumentException();
        }
    }

    @Override
    public boolean isExistsOrderTablesAndNotCompletion(List<Long> orderTableIds) {
        return orderRepository.existsByOrderTableIdInAndOrderStatusIn(
                orderTableIds, Arrays.asList(OrderStatus.COOKING.name(), OrderStatus.MEAL.name()));
    }
}
