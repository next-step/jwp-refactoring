package kitchenpos.order.service;

import java.util.Arrays;
import java.util.List;
import kitchenpos.order.domain.entity.OrderRepository;
import kitchenpos.order.domain.value.OrderStatus;
import kitchenpos.table.exception.OrderStatusInCookingOrMealException;
import org.springframework.stereotype.Component;

@Component
public class OrderValidatorInTable implements kitchenpos.table.service.OrderValidatorInTable {
    private final OrderRepository orderRepository;

    public OrderValidatorInTable(OrderRepository orderRepository) {
        this.orderRepository = orderRepository;
    }

    @Override
    public void validateOrderStatusInCookingOrMeal(List<Long> orderTableIds) {
        if (orderRepository.existsByOrderTableIdInAndOrderStatusIn(
            orderTableIds, Arrays.asList(OrderStatus.COOKING.name(), OrderStatus.MEAL.name()))) {
            throw new OrderStatusInCookingOrMealException();
        }
    }

    @Override
    public void validateOrderStatusInCookingOrMeal(Long orderTableId) {
        if (orderRepository.existsByOrderTableIdAndOrderStatusIn(
            orderTableId, Arrays.asList(OrderStatus.COOKING.name(), OrderStatus.MEAL.name()))) {
            throw new OrderStatusInCookingOrMealException();
        }
    }
}
