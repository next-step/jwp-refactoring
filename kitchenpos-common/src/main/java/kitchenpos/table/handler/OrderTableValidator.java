package kitchenpos.table.handler;

import kitchenpos.order.domain.Order;
import kitchenpos.order.domain.OrderRepository;
import kitchenpos.order.exception.NotFoundOrderException;
import kitchenpos.table.domain.OrderTable;
import kitchenpos.table.exception.NotChangeToEmptyThatCookingOrMealTable;
import org.springframework.stereotype.Component;

@Component
public class OrderTableValidator {
    private final OrderRepository orderRepository;

    public OrderTableValidator(OrderRepository orderRepository) {
        this.orderRepository = orderRepository;
    }

    public void validateToEmpty(OrderTable orderTable) {
        if (isCookingOrMeal(getOrder(orderTable.getId()))) {
            throw new NotChangeToEmptyThatCookingOrMealTable();
        }
    }

    private boolean isCookingOrMeal(Order order) {
        return order.isCookingOrMeal();
    }

    private Order getOrder(Long orderTableId) {
        return orderRepository.findByOrderTableId(orderTableId)
                .orElseThrow(() -> new NotFoundOrderException());
    }
}
