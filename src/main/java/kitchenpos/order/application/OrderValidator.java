package kitchenpos.order.application;

import java.util.Arrays;

import org.springframework.stereotype.Component;

import kitchenpos.order.domain.OrderRepository;
import kitchenpos.order.domain.OrderStatus;
import kitchenpos.order.exception.OrderAlreadyExistsException;

@Component
public class OrderValidator {

    private final OrderRepository orderRepository;

    public OrderValidator(OrderRepository orderRepository) {
        this.orderRepository = orderRepository;
    }

    public void validateExistsOrderStatusIsCookingANdMeal(Long orderTableId) {
        if (orderRepository.existsByOrderTableIdAndOrderStatusIn(
                orderTableId, Arrays.asList(OrderStatus.COOKING, OrderStatus.MEAL))) {
            throw new OrderAlreadyExistsException("주문 상태가 COOKING 또는 MEAL인 주문이 존재합니다. 입력 ID : " + orderTableId);
        }
    }
}
