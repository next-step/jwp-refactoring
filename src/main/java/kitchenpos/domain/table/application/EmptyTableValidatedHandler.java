package kitchenpos.domain.table.application;

import kitchenpos.domain.order.domain.EmptyTableValidatedEvent;
import kitchenpos.domain.table.domain.OrderTable;
import kitchenpos.domain.table.domain.OrderTableRepository;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Component
public class EmptyTableValidatedHandler  {

    private final OrderTableRepository orderTableRepository;

    public EmptyTableValidatedHandler(OrderTableRepository orderTableRepository) {
        this.orderTableRepository = orderTableRepository;
    }

    @EventListener
    public void handle(EmptyTableValidatedEvent event) {
        OrderTable orderTable = orderTableRepository.findById(event.getOrderTableId())
                .orElseThrow(IllegalArgumentException::new);

        if (orderTable.isEmpty()) {
            throw new IllegalArgumentException();
        }
    }
}
