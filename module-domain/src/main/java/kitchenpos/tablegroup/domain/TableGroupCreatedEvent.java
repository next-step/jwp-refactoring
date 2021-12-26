package kitchenpos.tablegroup.domain;


import kitchenpos.event.TableGroupCreatedEventRequest;
import org.springframework.context.ApplicationEvent;


public class TableGroupCreatedEvent extends ApplicationEvent {

    private final TableGroupCreatedEventRequest tableGroupCreatedEventRequest;


    public TableGroupCreatedEvent(TableGroupCreatedEventRequest tableGroupCreatedEventRequest) {
        super(tableGroupCreatedEventRequest);
        this.tableGroupCreatedEventRequest = tableGroupCreatedEventRequest;
    }

    public TableGroupCreatedEventRequest getTableGroupCreatedEventRequest() {
        return tableGroupCreatedEventRequest;
    }
}
