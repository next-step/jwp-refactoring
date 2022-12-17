package kitchenpos.table.event;

import org.springframework.context.ApplicationEvent;

import java.util.List;

public abstract class AllocatedGroupEvent extends ApplicationEvent {

    private static final long serialVersionUID = -2107995735418521734L;

    protected AllocatedGroupEvent(Object source) {
        super(source);
    }

    public abstract List<Long> getOrderTableIds();

    public abstract long getTableGroupId();
}
