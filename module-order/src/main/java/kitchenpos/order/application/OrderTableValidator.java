package kitchenpos.order.application;

import java.util.List;
import kitchenpos.order.domain.Order;
import kitchenpos.order.domain.OrderRepository;
import kitchenpos.order.exception.CannotChangeTableEmptyException;
import org.springframework.stereotype.Component;

@Component
public class OrderTableValidator {

    public static final String THERE_IS_AN_ONGOING_ORDER = "진행중(조리 or 식사)인 경우 빈 테이블로 변경이 불가능하다.";

    private final OrderRepository orderRepository;

    public OrderTableValidator(OrderRepository orderRepository) {
        this.orderRepository = orderRepository;
    }

    public void validationChangeEmpty(Long orderTableId) {
        List<Order> orders = orderRepository.findAllByOrderTableId(orderTableId);
        if (isNotCompleted(orders)) {
            throw new CannotChangeTableEmptyException(THERE_IS_AN_ONGOING_ORDER);
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
