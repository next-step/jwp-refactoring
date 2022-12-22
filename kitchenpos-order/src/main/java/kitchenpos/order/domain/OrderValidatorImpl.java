package kitchenpos.order.domain;

import kitchenpos.order.message.OrderMessage;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

@Component
public class OrderValidatorImpl implements OrderValidator {

    private static final List<OrderStatus> INVALID_ORDER_STATUS = Arrays.asList(OrderStatus.COOKING, OrderStatus.MEAL);

    private final OrderRepository orderRepository;

    public OrderValidatorImpl(OrderRepository orderRepository) {
        this.orderRepository = orderRepository;
    }

    public void validateOrderStatusIsCookingOrMealByTableId(Long orderTableId) {
        validateOrderStatusIsCookingOrMealByTableIds(Collections.singletonList(orderTableId));
    }

    public void validateOrderStatusIsCookingOrMealByTableIds(List<Long> orderTableIds) {
        if(orderRepository.existsByOrderTableIdInAndOrderStatusIn(orderTableIds, INVALID_ORDER_STATUS)) {
            throw new IllegalArgumentException(OrderMessage.ERROR_INVALID_ORDER_STATE.message());
        }
    }
}
