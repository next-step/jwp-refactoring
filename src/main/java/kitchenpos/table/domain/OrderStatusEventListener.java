package kitchenpos.table.domain;

import kitchenpos.exception.NotFoundException;
import kitchenpos.order.domain.OrderStatusEvent;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
public class OrderStatusEventListener {

    private final OrderTableRepository orderTableRepository;

    public OrderStatusEventListener(
        OrderTableRepository orderTableRepository) {
        this.orderTableRepository = orderTableRepository;
    }

    @Async
    @EventListener
    @Transactional
    public void listen(OrderStatusEvent event) {
        OrderTable orderTable = findOrderTable(event.getOrderTableId());
        orderTable.changeTableStatus(event.getStatus());
    }

    private OrderTable findOrderTable(Long orderTableId) {
        return orderTableRepository.findById(orderTableId)
            .orElseThrow(NotFoundException::new);
    }

}
