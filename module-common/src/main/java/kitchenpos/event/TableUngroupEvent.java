package kitchenpos.event;

import org.springframework.context.ApplicationEvent;

public class TableUngroupEvent extends ApplicationEvent {

    public TableUngroupEvent(Object source) {
        super(source);
    }
}