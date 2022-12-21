package kitchenpos.order.application.validator;

import static kitchenpos.exception.ErrorCode.EXISTS_NOT_COMPLETION_STATUS;

import java.util.Arrays;
import java.util.List;
import kitchenpos.exception.KitchenposException;
import kitchenpos.order.domain.OrderRepository;
import kitchenpos.order.domain.OrderStatus;
import org.springframework.stereotype.Component;

@Component
public class OrderStatusValidator implements kitchenpos.table.application.validator.OrderStatusValidator {
    private final OrderRepository orderRepository;

    public OrderStatusValidator(OrderRepository orderRepository) {
        this.orderRepository = orderRepository;
    }

    @Override
    public void existsByOrderTableIdAndOrderStatusIn(Long orderTableId, List<OrderStatus> asList) {
        if (orderRepository.existsByOrderTableIdAndOrderStatusIn(
                orderTableId,
                Arrays.asList(OrderStatus.COOKING, OrderStatus.MEAL))
        ) {
            throw new KitchenposException(EXISTS_NOT_COMPLETION_STATUS);
        }
    }
}
