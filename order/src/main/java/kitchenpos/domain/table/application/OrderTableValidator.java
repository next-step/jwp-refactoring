package kitchenpos.domain.table.application;

import kitchenpos.domain.order.domain.Order;
import kitchenpos.domain.order.domain.OrderRepository;
import kitchenpos.exception.BusinessException;
import kitchenpos.exception.ErrorCode;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class OrderTableValidator {

    private final OrderRepository orderRepository;

    public OrderTableValidator(OrderRepository orderRepository) {
        this.orderRepository = orderRepository;
    }

    public void validateCompleteTable(Long orderTableId) {
        final Optional<Order> savedOrder = orderRepository.findByOrderTableId(orderTableId);
        if (!savedOrder.isPresent()) {
            throw new BusinessException(ErrorCode.ORDER_NOT_FOUND);
        }
        final Order order = savedOrder.get();
        if (!order.isComplete()) {
            throw new BusinessException(ErrorCode.NOT_COMPLETE_ORDER);
        }
    }
}
