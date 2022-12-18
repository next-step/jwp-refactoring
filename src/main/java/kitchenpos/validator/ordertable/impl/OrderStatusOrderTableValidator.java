package kitchenpos.validator.ordertable.impl;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import kitchenpos.order.domain.Order;
import kitchenpos.order.domain.OrderStatus;
import kitchenpos.order.repository.OrderRepository;
import kitchenpos.ordertable.domain.OrderTable;
import kitchenpos.validator.ordertable.OrderTableValidator;
import org.springframework.stereotype.Component;

@Component
public class OrderStatusOrderTableValidator implements OrderTableValidator {

    private final OrderRepository orderRepository;

    public OrderStatusOrderTableValidator(OrderRepository orderRepository) {
        this.orderRepository = orderRepository;
    }

    @Override
    public void validate(OrderTable orderTable) {
        List<Order> orders = orderRepository.findByOrderTableId(orderTable.getId());
        List<String> orderStatuses = Arrays.asList(OrderStatus.COOKING.name(), OrderStatus.MEAL.name());
        Optional<Order> findInOrderStatuses = orders.stream()
                .filter(order -> order.isSameStatus(orderStatuses))
                .findAny();

        if (findInOrderStatuses.isPresent()) {
            throw new IllegalArgumentException("조리, 식사 상태의 주문이 포함 된 주문 테이블은 상태를 변경할 수 없습니다");
        }
    }
}
