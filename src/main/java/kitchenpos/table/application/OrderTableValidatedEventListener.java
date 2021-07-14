package kitchenpos.table.application;

import kitchenpos.order.domain.OrderTableValidatedEvent;
import kitchenpos.table.domain.OrderTable;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Component
public class OrderTableValidatedEventListener {
    private static final String ORDER_LINE_EMPTY = "주문 항목이 비어 있습니다.";
    private final OrderTableService orderTableService;

    public OrderTableValidatedEventListener(OrderTableService orderTableService) {
        this.orderTableService = orderTableService;
    }

    @EventListener
    public void validateOrderTable(OrderTableValidatedEvent orderTableValidatedEvent) {
        Long orderTableId = orderTableValidatedEvent.getOrderTableId();
        OrderTable orderTable = orderTableService.findOrderTable(orderTableId);

        if (orderTable.isEmpty()) {
            throw new IllegalStateException(ORDER_LINE_EMPTY);
        }
    }
}
