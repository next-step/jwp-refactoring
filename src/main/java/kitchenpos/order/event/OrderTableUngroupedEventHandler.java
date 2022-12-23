package kitchenpos.order.event;

import kitchenpos.constants.ErrorMessages;
import kitchenpos.order.domain.Order;
import kitchenpos.order.domain.OrderRepository;
import kitchenpos.ordertable.event.OderTableUngroupedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
public class OrderTableUngroupedEventHandler {

    private final OrderRepository orderRepository;

    public OrderTableUngroupedEventHandler(OrderRepository orderRepository) {
        this.orderRepository = orderRepository;
    }

    @EventListener
    @Transactional
    public void handle(OderTableUngroupedEvent event) {
        if (orderRepository.findByOrderTableId(event.getOrderTableId()).stream()
                .anyMatch(Order::isOrderStatusNotComplete)) {
            throw new IllegalArgumentException(ErrorMessages.NOT_COMPLETED_ORDER_EXIST);
        }
    }
}
