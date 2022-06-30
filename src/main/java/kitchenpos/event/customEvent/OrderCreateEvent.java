package kitchenpos.event.customEvent;

import org.springframework.context.ApplicationEvent;

public class OrderCreateEvent extends ApplicationEvent {

    public OrderCreateEvent(Object source) {
        super(source);
    }
}
