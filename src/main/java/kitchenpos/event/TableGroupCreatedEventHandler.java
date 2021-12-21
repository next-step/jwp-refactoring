package kitchenpos.event;

import kitchenpos.domain.TableGroup;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;


@Component
public class TableGroupCreatedEventHandler {

    @EventListener
    public void createTableGroup(TableGroupCreatedEvent event) {

        TableGroup tableGroup = event.getTableGroup();
        tableGroup.getOrderTables()
                    .forEach(orderTable -> {
                        orderTable.checkAvailable();
                        orderTable.changeNonEmptyOrderTable();
                    });

    }


}
