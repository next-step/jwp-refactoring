package kitchenpos.event.table;

import kitchenpos.domain.table.TableGroup;
import org.springframework.context.ApplicationEvent;

public class TableGroupCreatedEvent extends ApplicationEvent {

    private final TableGroup tableGroup;

    public TableGroupCreatedEvent(TableGroup tableGroup) {
        super(tableGroup);
        this.tableGroup = tableGroup;
    }

    public TableGroup getTableGroup() {
        return tableGroup;
    }
}
