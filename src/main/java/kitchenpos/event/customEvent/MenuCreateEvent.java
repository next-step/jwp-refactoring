package kitchenpos.event.customEvent;

import org.springframework.context.ApplicationEvent;

public class MenuCreateEvent extends ApplicationEvent {

    public MenuCreateEvent(Object source) {
        super(source);
    }
}
