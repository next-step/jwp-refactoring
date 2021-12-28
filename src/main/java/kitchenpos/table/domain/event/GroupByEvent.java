package kitchenpos.table.domain.event;

import org.springframework.context.ApplicationEvent;

public class GroupByEvent extends ApplicationEvent {

    private GroupTable groupTable;

    public GroupByEvent(Object source, GroupTable groupTable) {
        super(source);
        this.groupTable = groupTable;
    }

    public GroupTable getGroupTable() {
        return groupTable;
    }
}
