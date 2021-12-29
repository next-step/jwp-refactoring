package kitchenpos.order.domain;

import kitchenpos.common.event.OrderTableChangeOrderCloseEvent;
import kitchenpos.order.exception.OrderIsNotCompleteException;
import java.util.List;

import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

@Component
public class OrderValidateEventListener {

    private final OrderRepository orderRepository;

    public OrderValidateEventListener(OrderRepository orderRepository) {
        this.orderRepository = orderRepository;
    }

    @TransactionalEventListener(phase = TransactionPhase.BEFORE_COMMIT)
    public void validateAllOrdersInTableComplete(
        OrderTableChangeOrderCloseEvent orderValidateEvent) {
        Long orderTableId = orderValidateEvent.getOrderTableId();
        List<Order> orders = orderRepository.findAllByOrderTableId(orderTableId);
        orders.stream()
            .forEach(order -> validateOrderIsComplete(order));
    }

    private void validateOrderIsComplete(Order order) {
        if (!order.isCompleteStatus()) {
            throw new OrderIsNotCompleteException();
        }
    }

}
