package table.application;

import order.domain.OrderCreatedEvent;
import order.exception.NotCreateOrderException;
import order.exception.OrderErrorCode;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;
import table.domain.OrderTable;
import table.domain.OrderTableRepository;
import table.exception.NotFoundOrderTableException;

@Component
public class OrderCreateEventHandler {

    private final OrderTableRepository orderTableRepository;

    public OrderCreateEventHandler(OrderTableRepository orderTableRepository) {
        this.orderTableRepository = orderTableRepository;
    }

    @EventListener(OrderCreatedEvent.class)
    public void handle(OrderCreatedEvent event) {
        Long orderTableId = event.getOrderTableId();

        OrderTable orderTable = orderTableRepository.findById(orderTableId)
                .orElseThrow(() -> new NotFoundOrderTableException(orderTableId));

        if (orderTable.isEmpty()) {
            throw new NotCreateOrderException(orderTable.getId() + OrderErrorCode.EMPTY_ORDER_TABLE);
        }

    }

}
