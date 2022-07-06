package kitchenpos.order.event;

import kitchenpos.common.exception.NotCompletionStatusException;
import kitchenpos.order.repository.OrderRepository;
import kitchenpos.table.domain.OrderTable;
import kitchenpos.table.event.ChangeEmptyTableEventPublisher;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
public class ChangeEmptyTableEventHandler {
    private final OrderRepository orderRepository;

    public ChangeEmptyTableEventHandler(OrderRepository orderRepository) {
        this.orderRepository = orderRepository;
    }

    @EventListener
    @Transactional
    public void handle(ChangeEmptyTableEventPublisher event) {
        final OrderTable orderTable = event.orderTable();
        validateNotCompletionOrderTable(orderTable.getId());
    }

    public void validateNotCompletionOrderTable(Long orderTableId) {
        if (orderRepository.existNotCompletionOrderTable(orderTableId)) {
            throw new NotCompletionStatusException();
        }
    }
}
