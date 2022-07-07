package kitchenpos.order.application;

import java.util.List;
import kitchenpos.order.domain.OrderStatus;
import kitchenpos.order.repository.OrderRepository;
import kitchenpos.table.application.OrderTableOrderStatusValidator;
import org.springframework.stereotype.Component;

@Component
public class OrderTableOrderStatusValidatorImpl implements OrderTableOrderStatusValidator {

    public static final String COOKING_OR_MEAL_ORDER_TABLE_DEALLOCATE_ERROR_MESSAGE = "조리중, 식사중인 주문 테이블이 포함되어 있어 단체 지정을 해제 할 수 없습니다.";

    private final OrderRepository orderRepository;

    public OrderTableOrderStatusValidatorImpl(OrderRepository orderRepository) {
        this.orderRepository = orderRepository;
    }

    @Override
    public void validateOrderStatus(List<Long> orderTableIds) {
        if (orderRepository.existsByOrderTableIdInAndOrderStatusIn(orderTableIds, OrderStatus.canNotChangeOrderTableStatuses())) {
            throw new IllegalArgumentException(COOKING_OR_MEAL_ORDER_TABLE_DEALLOCATE_ERROR_MESSAGE);
        }
    }
}
