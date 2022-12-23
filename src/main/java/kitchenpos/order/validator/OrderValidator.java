package kitchenpos.order.validator;

import kitchenpos.order.domain.Order;
import kitchenpos.order.repository.OrderRepository;
import org.springframework.stereotype.Component;

import javax.persistence.EntityNotFoundException;

@Component
public class OrderValidator {

    public static final String ORDER_STATUS_NOT_COMPLETION_EXCEPTION_MESSAGE = "완료 상태만 변경 가능합니다";

    private final OrderRepository orderRepository;

    public OrderValidator(OrderRepository orderRepository) {
        this.orderRepository = orderRepository;
    }

    public void validateNotComplete(Long orderTableId) {
        Order order = orderRepository.findByOrderTableId(orderTableId).orElseThrow(EntityNotFoundException::new);
        if (!order.isComplete()) {
            throw new IllegalArgumentException(ORDER_STATUS_NOT_COMPLETION_EXCEPTION_MESSAGE);
        }
    }
}
