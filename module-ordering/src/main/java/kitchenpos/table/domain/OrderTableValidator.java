package kitchenpos.table.domain;

import kitchenpos.ordering.domain.OrderRepository;
import kitchenpos.ordering.domain.OrderStatus;
import org.springframework.stereotype.Component;

import java.util.Arrays;

@Component
public class OrderTableValidator {
    private final OrderRepository orderRepository;

    public OrderTableValidator(OrderRepository orderRepository) {
        this.orderRepository = orderRepository;
    }

    public void validate(OrderTable orderTable) {
        orderTable.checkIfAlreadyGrouped();

        checkIfAllOfOrderInOrderTableIsCompleted(orderTable.getId());
    }

    private void checkIfAllOfOrderInOrderTableIsCompleted(Long orderTableId) {
        if (orderRepository.existsByOrderTableIdAndOrderStatusIn(
                orderTableId, Arrays.asList(OrderStatus.COOKING, OrderStatus.MEAL))) {
            throw new IllegalArgumentException("주문테이블에 아직 완료되지 않은 주문이 있습니다.");
        }
    }
}
