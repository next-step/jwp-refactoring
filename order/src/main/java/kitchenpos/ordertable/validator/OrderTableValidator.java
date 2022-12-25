package kitchenpos.ordertable.validator;

import java.util.Objects;
import java.util.Optional;
import kitchenpos.order.repository.OrderRepository;
import kitchenpos.order.domain.Order;
import kitchenpos.ordertable.domain.OrderTable;
import org.springframework.stereotype.Component;

@Component
public class OrderTableValidator {

    private final OrderRepository orderRepository;

    public OrderTableValidator(OrderRepository orderRepository) {
        this.orderRepository = orderRepository;
    }

    public void validateChangeEmpty(OrderTable orderTable) {
        if (Objects.nonNull(orderTable.getTableGroupId())) {
            throw new IllegalArgumentException("단체 지정된 테이블은 비울 수 없습니다.");
        }

        Optional<Order> orderByTableId = findOrderByTableId(orderTable.getId());
        if (orderByTableId.filter(Order::onCookingOrMeal).isPresent()) {
            throw new IllegalArgumentException("조리중 이거나 식사중에는 테이블을 비울 수 없습니다.");
        }
    }

    private Optional<Order> findOrderByTableId(Long tableId) {
        return orderRepository.findByOrderTableId(tableId);
    }
}
