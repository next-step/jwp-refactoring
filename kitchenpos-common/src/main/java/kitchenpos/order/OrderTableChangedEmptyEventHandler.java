package kitchenpos.order;

import static kitchenpos.order.OrderStatus.CHANGE_EMPTY_IMPOSSIBLE_ORDER_STATUS;


import kitchenpos.table.OrderTableChangedEmptyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
public class OrderTableChangedEmptyEventHandler {

    private final OrderRepository orderRepository;

    public OrderTableChangedEmptyEventHandler(OrderRepository orderRepository) {
        this.orderRepository = orderRepository;
    }

    @EventListener
    @Transactional
    public void handler(OrderTableChangedEmptyEvent event) {
        validateChangeableOrderStatus(event.getOrderTableId());
    }

    private void validateChangeableOrderStatus(Long orderTableId) {
        if (orderRepository.existsByOrderTableIdAndOrderStatusIn(orderTableId,
            CHANGE_EMPTY_IMPOSSIBLE_ORDER_STATUS)) {
            throw new IllegalArgumentException();
        }
    }
}
