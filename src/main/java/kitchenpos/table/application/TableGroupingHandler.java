package kitchenpos.table.application;

import kitchenpos.table.domain.OrderTable;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.Table;
import java.util.List;

@Component
public class TableGroupingHandler implements ApplicationListener<TableGroupingEvent> {

    private final TableService tableService;

    public TableGroupingHandler(final TableService tableService) {
        this.tableService = tableService;
    }

    @Transactional
    @Override
    public void onApplicationEvent(TableGroupingEvent event) {
        final List<OrderTable> orderTables = tableService.findAllByIds(event.getOrderTableIds());
        final Long tableGroupId = event.getTableGroupId();

        for(OrderTable orderTable : orderTables)
        {
            orderTable.validateFullAndTableGrouping();
            orderTable.full();
            orderTable.grouping(tableGroupId);
        }
    }
}
