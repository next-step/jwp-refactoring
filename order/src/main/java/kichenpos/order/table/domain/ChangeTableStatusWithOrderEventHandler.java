package kichenpos.order.table.domain;

import kichenpos.order.order.domain.event.OrderCreatedEvent;
import kichenpos.order.order.domain.event.OrderStatusChangedEvent;
import kichenpos.order.table.infrastructure.OrderTableClient;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

@Component
public class ChangeTableStatusWithOrderEventHandler {

    private final OrderTableClient tableClient;

    public ChangeTableStatusWithOrderEventHandler(OrderTableClient tableClient) {
        this.tableClient = tableClient;
    }

    @Async
    @EventListener
    public void orderTable(OrderCreatedEvent createdEvent) {
        tableClient.changeOrdered(createdEvent.tableId());
    }

    @Async
    @EventListener
    public void finishTable(OrderStatusChangedEvent statusChangedEvent) {
        if (statusChangedEvent.isCompleted()) {
            tableClient.changeFinish(statusChangedEvent.tableId());
        }
    }
}
