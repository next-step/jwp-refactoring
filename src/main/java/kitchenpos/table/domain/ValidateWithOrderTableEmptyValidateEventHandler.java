package kitchenpos.table.domain;

import kitchenpos.order.domain.OrderTableEmptyValidateEvent;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
public class ValidateWithOrderTableEmptyValidateEventHandler {
    private final OrderTableRepository orderTableRepository;

    public ValidateWithOrderTableEmptyValidateEventHandler(OrderTableRepository orderTableRepository) {
        this.orderTableRepository = orderTableRepository;
    }

    @Async
    @EventListener
    @Transactional
    public void handle(OrderTableEmptyValidateEvent event) {
        OrderTable orderTable = getOrderTable(event.getOrderTableId());

        if (orderTable.isEmpty()) {
            throw new IllegalArgumentException();
        }
    }

    private OrderTable getOrderTable(Long id) {
        return orderTableRepository.findById(id)
                .orElseThrow(IllegalArgumentException::new);
    }
}
