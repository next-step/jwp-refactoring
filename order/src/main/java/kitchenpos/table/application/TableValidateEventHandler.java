package kitchenpos.table.application;

import kitchenpos.order.domain.OrderTableValidateEvent;
import kitchenpos.table.domain.OrderTable;
import kitchenpos.table.domain.OrderTableRepository;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Component
public class TableValidateEventHandler {
    private final OrderTableRepository orderTableRepository;

    public TableValidateEventHandler(OrderTableRepository orderTableRepository) {
        this.orderTableRepository = orderTableRepository;
    }

    @EventListener
    public void orderTableValidateEventListener(OrderTableValidateEvent orderTableValidateEvent) {
        OrderTable orderTable = orderTableRepository.findById(orderTableValidateEvent.getOrderTableId())
                .orElseThrow(IllegalArgumentException::new);

        if (orderTable.isEmpty()) {
            throw new IllegalArgumentException();
        }

        System.out.println("orderTableValidateEventListener ------------ SUCCESS ");
    }
}
