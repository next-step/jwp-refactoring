package kitchenpos.order.application.validator;

import static kitchenpos.exception.ErrorCode.EXISTS_NOT_COMPLETION_STATUS;

import java.util.Arrays;
import java.util.List;
import kitchenpos.exception.KitchenposException;
import kitchenpos.order.domain.OrderRepository;
import kitchenpos.order.domain.OrderStatus;
import org.springframework.stereotype.Component;

@Component
public class OrderStatusValidatorImpl implements kitchenpos.table.application.validator.OrderStatusValidator {
    private final OrderRepository orderRepository;

    public OrderStatusValidatorImpl(OrderRepository orderRepository) {
        this.orderRepository = orderRepository;
    }

    @Override
    public void existsByOrderTableIdAndOrderStatusIn(Long orderTableId) {
        if (orderRepository.existsByOrderTableIdAndOrderStatusIn(
                orderTableId,
                Arrays.asList(OrderStatus.COOKING, OrderStatus.MEAL))
        ) {
            throw new KitchenposException(EXISTS_NOT_COMPLETION_STATUS);
        }
    }

    @Override
    public void existsByOrderTableIdInAndOrderStatusIn(List<Long> orderTableIds) {
        if (orderRepository.existsByOrderTableIdInAndOrderStatusIn(
                orderTableIds, Arrays.asList(OrderStatus.COOKING, OrderStatus.MEAL))) {
            throw new KitchenposException(EXISTS_NOT_COMPLETION_STATUS);
        }
    }
}
