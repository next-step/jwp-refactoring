package kitchenpos.table.application;

import kitchenpos.order.domain.OrderCreatedEvent;
import kitchenpos.order.exception.NotCreateOrderException;
import kitchenpos.order.exception.OrderErrorCode;
import kitchenpos.table.domain.OrderTableRepository;
import kitchenpos.table.exception.NotFoundOrderTableException;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;
import kitchenpos.table.domain.OrderTable;

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
