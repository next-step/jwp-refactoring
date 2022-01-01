package kitchenpos.order.domain;

import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import kitchenpos.common.exception.KitchenposErrorCode;
import kitchenpos.common.exception.KitchenposException;
import kitchenpos.table.domain.OrderStatusValidateEvent;

@Component
public class OrderStatusValidateEventHandler {
    private final OrderRepository orderRepository;

    public OrderStatusValidateEventHandler(OrderRepository orderRepository) {
        this.orderRepository = orderRepository;
    }

    @EventListener
    @Transactional
    public void handle(OrderStatusValidateEvent event) {
        if (orderRepository.existsByOrderTableIdAndOrderStatusIn(
            event.getOrderTableId(), OrderStatus.NOT_COMPLETED_LIST)) {
            throw new KitchenposException(KitchenposErrorCode.CONTAINS_USED_TABLE);
        }
    }
}
