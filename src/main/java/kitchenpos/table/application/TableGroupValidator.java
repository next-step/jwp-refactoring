package kitchenpos.table.application;

import java.util.List;
import kitchenpos.order.domain.Order;
import kitchenpos.order.domain.OrderRepository;
import kitchenpos.table.exception.CannotUngroupOrderTableException;
import org.springframework.stereotype.Component;

@Component
public class TableGroupValidator {

    public static final String THERE_IS_A_HISTORY_OF_ORDERS_AT_AN_ONGOING = "진행중(조리 or 식사)인 단계의 주문 이력이 존재할 경우 해제가 불가능하다.";

    private final OrderRepository orderRepository;

    public TableGroupValidator(OrderRepository orderRepository) {
        this.orderRepository = orderRepository;
    }

    public void validationUpgroup(List<Long> orderTableIds) {
        for (Long orderTableId : orderTableIds) {
            validationOrdersForUngroup(orderRepository.findAllByOrderTableId(orderTableId));
        }
    }

    private void validationOrdersForUngroup(List<Order> orders) {
        if (isNotCompleted(orders)) {
            throw new CannotUngroupOrderTableException(THERE_IS_A_HISTORY_OF_ORDERS_AT_AN_ONGOING);
        }
    }

    private boolean isNotCompleted(List<Order> orders) {
        if (orders.isEmpty()) {
            return false;
        }
        return orders.stream()
            .noneMatch(Order::isCompleted);
    }
}
