package kitchenpos.table.application;

import kitchenpos.table.domain.OrderTable;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.ApplicationListener;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public class TableUnGroupingHandler implements ApplicationListener<TableUnGroupingEvent> {

    private final TableService tableService;

    public TableUnGroupingHandler(final TableService tableService) {
        this.tableService = tableService;
    }

    @Transactional
    @Override
    public void onApplicationEvent(TableUnGroupingEvent event) {
        final Long tableGroupId = event.getTableGroupId();
        final List<OrderTable> orderTables = tableService.findAllByTableGroupId(tableGroupId);

        for(OrderTable orderTable : orderTables) {
            orderTable.unGrouping();
        }
    }

}
