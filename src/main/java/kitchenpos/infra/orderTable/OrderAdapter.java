package kitchenpos.infra.orderTable;

import kitchenpos.domain.order.OrderRepository;
import kitchenpos.domain.order.OrderStatus;
import kitchenpos.domain.orderTable.SafeOrder;
import kitchenpos.domain.orderTable.exceptions.InvalidTryChangeEmptyException;
import org.springframework.stereotype.Component;

import java.util.Arrays;

@Component
public class OrderAdapter implements SafeOrder {
    private final OrderRepository orderRepository;

    public OrderAdapter(final OrderRepository orderRepository) {
        this.orderRepository = orderRepository;
    }

    @Override
    public void canChangeEmptyStatus(final Long orderTableId) {
        if (orderRepository.existsByOrderTableIdAndOrderStatusIn(
                orderTableId, Arrays.asList(OrderStatus.COOKING, OrderStatus.MEAL))) {
            throw new InvalidTryChangeEmptyException("조리중이거나 식사중인 주문 테이블의 비움 상태를 바꿀 수 없습니다.");
        }
    }
}
