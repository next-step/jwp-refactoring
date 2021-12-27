package kitchenpos.tablegroup.event;

import org.springframework.context.ApplicationEvent;

public class UngroupEvent extends ApplicationEvent {

    private Long tableGroupId;

    public UngroupEvent(Object source, Long tableGroupId) {
        super(source);
        this.tableGroupId = tableGroupId;
    }

    public Long getTableGroupId() {
        return tableGroupId;
    }
}
