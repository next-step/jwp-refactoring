package kitchenpos.ordertable.domain;

import java.util.List;
import kitchenpos.common.exception.CommonErrorCode;
import kitchenpos.common.exception.InvalidParameterException;
import kitchenpos.order.domain.Order;
import kitchenpos.order.domain.OrderRepository;
import org.springframework.stereotype.Component;

@Component
public class TableValidator {

    private final OrderRepository orderRepository;

    public TableValidator(final OrderRepository orderRepository) {
        this.orderRepository = orderRepository;
    }

    public void completedOrderValid(OrderTable orderTable) {
        List<Order> orders = getOrders(orderTable.getId());
        boolean isAllCompleted = orders.stream()
            .allMatch(Order::isComplete);

        if (!isAllCompleted) {
            throw new InvalidParameterException(
                CommonErrorCode.ORDER_TABLE_CHANGE_EMPTY_NOT_COMPLETE_EXCEPTION);
        }
    }

    private List<Order> getOrders(Long orderTableId) {
        return orderRepository.findAllByOrderTableId(orderTableId);
    }
}
