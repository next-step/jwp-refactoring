package kitchenpos.table.application;

import kitchenpos.order.dto.OrderCompletionEvent;
import kitchenpos.table.domain.OrderTable;
import kitchenpos.table.domain.TableRepository;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Component
public class OrderCompletionEventHandler {
    private final TableRepository tableRepository;

    public OrderCompletionEventHandler(final TableRepository tableRepository) {
        this.tableRepository = tableRepository;
    }

    @EventListener
    public void orderCompletionEventListener(final OrderCompletionEvent event) {
        Long orderTableId = event.getOrderTableId();
        OrderTable table = tableRepository.getById(orderTableId);
        table.changeEmpty(true);
    }
}
