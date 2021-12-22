package kitchenpos.table.domain;

import kitchenpos.exception.NotFoundException;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
public class OrderTableStatusEventListener {

    private final OrderTableRepository orderTableRepository;

    public OrderTableStatusEventListener(
        OrderTableRepository orderTableRepository) {
        this.orderTableRepository = orderTableRepository;
    }

    @Async
    @EventListener
    @Transactional
    public void listen(OrderTableStatusEvent event) {
        OrderTable orderTable = findOrderTable(event.getOrderTableId());
        orderTable.updateOrderInfo(event.getOrderId(), event.getTableStatus());
    }

    private OrderTable findOrderTable(Long orderTableId) {
        return orderTableRepository.findById(orderTableId)
            .orElseThrow(NotFoundException::new);
    }

}
