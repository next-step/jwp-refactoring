package kitchenpos.table.application;

import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Component
public class TableEventHandler {
    private final TableService tableService;

    public TableEventHandler(TableService tableService) {
        this.tableService = tableService;
    }

    @EventListener
    public void grouped(TableEvent.Grouped event) {
        tableService.grouped(event.getTableGroupId(), event.getOrderTableIds());
    }

    @EventListener
    public void ungrouped(TableEvent.Ungrouped event) {
        tableService.ungrouped(event.getTableGroupId());
    }
}
