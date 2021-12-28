package kitchenpos.table.domain.event;


import org.springframework.context.ApplicationEvent;

public class UnGroupByEvent extends ApplicationEvent {

    private Long tableGroupId;

    public UnGroupByEvent(Object source, Long tableGroupId) {
        super(source);
        this.tableGroupId = tableGroupId;
    }

    public Long getGroupTableId() {
        return tableGroupId;
    }
}
