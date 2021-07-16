package kitchenpos.application.order;

import static kitchenpos.exception.KitchenposExceptionMessage.EXISTS_NOT_COMPLETION_ORDER;

import java.util.List;
import kitchenpos.application.table.OrderValidator;
import kitchenpos.domain.order.OrderRepository;
import kitchenpos.domain.order.OrderStatus;
import kitchenpos.exception.KitchenposException;
import org.springframework.stereotype.Component;

@Component
public class OrderValidateImpl implements OrderValidator {

    private final OrderRepository orderRepository;

    public OrderValidateImpl(final OrderRepository orderRepository) {
        this.orderRepository = orderRepository;
    }

    @Override
    public void checkNotCompletionOrders(List<Long> orderTableIds) {
        if (orderRepository.existsByOrderTableIdInAndOrderStatusIn(orderTableIds,
                                                                   OrderStatus.excludeCompletionList())) {
            throw new KitchenposException(EXISTS_NOT_COMPLETION_ORDER);
        }
    }

    @Override
    public void checkNotCompletionOrder(Long orderTableId) {
        if (orderRepository.existsByOrderTableIdAndOrderStatusIn(orderTableId,
                                                                 OrderStatus.excludeCompletionList())) {
            throw new KitchenposException(EXISTS_NOT_COMPLETION_ORDER);
        }
    }
}
