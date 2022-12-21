package kitchenpos.ordertable.domain;

import kitchenpos.constants.ErrorMessages;
import kitchenpos.order.domain.Order;
import kitchenpos.order.domain.OrderRepository;
import org.springframework.stereotype.Component;

@Component
public class OrderTableValidator {

    private final OrderRepository orderRepository;

    public OrderTableValidator(OrderRepository orderRepository) {
        this.orderRepository = orderRepository;
    }

    public void validateEmptyChangable(OrderTable orderTable) {
        checkOrderTableGrouped(orderTable);
        checkNotCompletedOrderExist(orderTable);
    }

    private void checkOrderTableGrouped(OrderTable orderTable) {
        if (orderTable.isGrouped()) {
            throw new IllegalArgumentException(ErrorMessages.GROUPED_ORDER_TABLE_CANNOT_CHANGE_EMPTY);
        }
    }

    private void checkNotCompletedOrderExist(OrderTable orderTable) {
        if (orderRepository.findByOrderTableId(orderTable.getId()).stream()
                .anyMatch(Order::isOrderStatusNotComplete)) {
            throw new IllegalArgumentException(ErrorMessages.CANNOT_CHANGE_EMPTY_IF_NOT_COMPLETED_ORDER_EXIST);
        }
    }
}
