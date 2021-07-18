package kitchenpos.order.publisher;

import kitchenpos.order.dto.OrderRequest;
import kitchenpos.order.event.OrderValidEvent;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;

@Component
public class OrderEventPublisher {

    private final ApplicationEventPublisher eventPublisher;

    public OrderEventPublisher(ApplicationEventPublisher eventPublisher) {
        this.eventPublisher = eventPublisher;
    }

    public void createOrderValidPublishEvent(OrderRequest orderRequest) {
        OrderValidEvent orderValidEvent = new OrderValidEvent(orderRequest.toMenuIds(), orderRequest.getOrderTableId());
        eventPublisher.publishEvent(orderValidEvent);
    }
}
