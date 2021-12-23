package kitchenpos.event;

import kitchenpos.domain.table.OrderTable;
import kitchenpos.domain.table.TableGroup;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import java.util.List;


@Component
public class TableGroupCreatedEventHandler {

    @EventListener
    public void nonEmptyOrderTables(TableGroupCreatedEvent event) {

        List<OrderTable> orderTables = event.getOrderTables();
        orderTables
                    .forEach(orderTable -> {
                        orderTable.checkAvailable();
                        orderTable.changeNonEmptyOrderTable();
                    });

    }


}
