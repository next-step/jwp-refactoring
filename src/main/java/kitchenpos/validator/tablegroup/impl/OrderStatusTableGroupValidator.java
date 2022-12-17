package kitchenpos.validator.tablegroup.impl;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import kitchenpos.order.domain.Order;
import kitchenpos.order.domain.OrderStatus;
import kitchenpos.order.repository.OrderRepository;
import kitchenpos.ordertable.domain.OrderTable;
import kitchenpos.validator.tablegroup.TableGroupValidator;
import org.springframework.stereotype.Component;

@Component
@org.springframework.core.annotation.Order(4)
public class OrderStatusTableGroupValidator extends TableGroupValidator {

    private final OrderRepository orderRepository;

    public OrderStatusTableGroupValidator(OrderRepository orderRepository) {
        this.orderRepository = orderRepository;
    }

    @Override
    protected void validate(List<OrderTable> orderTables) {
        List<String> orderStatuses = Arrays.asList(OrderStatus.COOKING.name(), OrderStatus.MEAL.name());
        orderTables.forEach(orderTable -> {
            Optional<Order> findSameStatus = orderRepository.findByOrderTableId(orderTable.getId())
                    .stream()
                    .filter(order -> order.isSameStatus(orderStatuses))
                    .findAny();
            if (findSameStatus.isPresent()) {
                throw new IllegalArgumentException("조리, 식사 상태의 주문이 포함 된 주문 테이블은 상태를 변경할 수 없습니다");
            }
        });
    }
}
