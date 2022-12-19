package kitchenpos.order.domain;

import kitchenpos.ExceptionMessage;
import org.springframework.stereotype.Component;

@Component
public class OrderValidator {

    private OrderRepository orderRepository;

    public OrderValidator(OrderRepository orderRepository) {
        this.orderRepository = orderRepository;
    }

    public Order findOrderByOrderTableId(Long orderTableId) {
        return orderRepository.findOrderByOrderTableId(orderTableId)
                .orElseThrow(() -> new IllegalArgumentException(ExceptionMessage.NOT_EXIST_ORDER_TABLE.getMessage()));
    }

    public void checkCookingOrMeal(Long id) {
        Order order = orderRepository.findOrderByOrderTableId(id)
                .orElseThrow(() -> new IllegalArgumentException(ExceptionMessage.NOT_EXIST_ORDER.getMessage()));
        order.checkCookingOrMeal();
    }
}
