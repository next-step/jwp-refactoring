package kitchenpos.tablegroup.validator;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import kitchenpos.order.domain.Order;
import kitchenpos.order.domain.OrderStatus;
import kitchenpos.order.repository.OrderRepository;
import kitchenpos.ordertable.domain.OrderTable;
import org.springframework.stereotype.Component;

@Component
public class OrderStatusTableGroupValidator implements TableUnGroupValidator {

    private final OrderRepository orderRepository;

    public OrderStatusTableGroupValidator(OrderRepository orderRepository) {
        this.orderRepository = orderRepository;
    }

    @Override
    public void validate(List<OrderTable> orderTables) {
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
