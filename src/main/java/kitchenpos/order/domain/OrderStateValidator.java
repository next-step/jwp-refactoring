package kitchenpos.order.domain;

import kitchenpos.orderconstants.OrderErrorMessages;
import kitchenpos.ordertable.domain.OrderTable;
import kitchenpos.ordertable.domain.OrderTableConditionValidator;
import org.springframework.stereotype.Component;

@Component
public class OrderStateValidator implements OrderTableConditionValidator {

    private final OrderRepository orderRepository;

    public OrderStateValidator(OrderRepository orderRepository) {
        this.orderRepository = orderRepository;
    }

    @Override
    public void checkNotCompletedOrderExist(OrderTable orderTable) {
        if (orderRepository.findByOrderTableId(orderTable.getId()).stream()
                .anyMatch(Order::isOrderStatusNotComplete)) {
            throw new IllegalArgumentException(OrderErrorMessages.CANNOT_CHANGE_EMPTY_IF_NOT_COMPLETED_ORDER_EXIST);
        }
    }
}
