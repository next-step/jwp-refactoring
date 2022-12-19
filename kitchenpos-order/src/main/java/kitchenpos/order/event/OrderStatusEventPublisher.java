package kitchenpos.order.event;

import org.springframework.context.ApplicationEvent;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;

@Component
public class OrderStatusEventPublisher {

    private final ApplicationEventPublisher applicationEventPublisher;

    public OrderStatusEventPublisher(ApplicationEventPublisher applicationEventPublisher) {
        this.applicationEventPublisher = applicationEventPublisher;
    }

    public void publish(ApplicationEvent applicationEvent) {
        applicationEventPublisher.publishEvent(applicationEvent);
    }
}
