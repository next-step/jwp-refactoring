package kitchenpos.event;

import org.springframework.context.ApplicationEvent;

public class TableChangeEmptyEvent extends ApplicationEvent {

    public TableChangeEmptyEvent(Object source) {
        super(source);
    }
}