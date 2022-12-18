package kitchenpos.table.event;

import org.springframework.context.ApplicationEvent;

public abstract class DeAllocatedGroupEvent extends ApplicationEvent {

    private static final long serialVersionUID = 2352328763146346586L;

    protected DeAllocatedGroupEvent(Object source) {
        super(source);
    }

    public abstract long getTableGroupId();
}
