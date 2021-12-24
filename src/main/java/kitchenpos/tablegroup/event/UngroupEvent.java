package kitchenpos.tablegroup.event;

import org.springframework.context.ApplicationEvent;

public class UngroupEvent extends ApplicationEvent {
    public UngroupEvent(Long tableGroupId) {
        super(tableGroupId);
    }

    public Long getTableGroupId() {
        return (Long) super.getSource();
    }
}
