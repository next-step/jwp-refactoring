package kitchenpos.ordertable.event;

import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import kitchenpos.ordertable.application.TableService;

@Component
public class GroupEventHandler {
    private final TableService tableService;

    public GroupEventHandler(TableService tableService) {
        this.tableService = tableService;
    }

    @EventListener
    public void groupCreatedEventHandler(GroupCreatedEvent groupCreatedEvent) {
        tableService.setGroup(groupCreatedEvent.getTableGroup(), groupCreatedEvent.getOrderTableIds());
    }

    @EventListener
    public void ungroupedEventHandler(UnGroupedEvent unGroupedEvent) {
        tableService.ungroup(unGroupedEvent.getTableGroup());
    }
}
