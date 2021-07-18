package kitchenpos.table.publisher;

import kitchenpos.table.domain.OrderTable;
import kitchenpos.table.domain.OrderTables;
import kitchenpos.table.domain.TableGroup;
import kitchenpos.table.event.OrderTableChangeEmptyValidEvent;
import kitchenpos.table.event.OrderTableGroupEvent;
import kitchenpos.table.event.OrderTableUngroupEvent;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class TableEventPublisher {

    private final ApplicationEventPublisher eventPublisher;

    public TableEventPublisher(ApplicationEventPublisher eventPublisher) {
        this.eventPublisher = eventPublisher;
    }


    public void changeEmptyValidPublishEvent(OrderTable orderTable) {
        OrderTableChangeEmptyValidEvent orderTableChangeEmptyValidEvent = new OrderTableChangeEmptyValidEvent(orderTable);
        eventPublisher.publishEvent(orderTableChangeEmptyValidEvent);
    }

    public void ungroupEventPublish(TableGroup tableGroup) {
        OrderTableUngroupEvent orderTableUngroupEvent = new OrderTableUngroupEvent(tableGroup);
        eventPublisher.publishEvent(orderTableUngroupEvent);

    }

    public void groupEventPublish(List<Long> tableGroupIds, OrderTables orderTables) {
        OrderTableGroupEvent orderTableGroupEvent = new OrderTableGroupEvent(tableGroupIds, orderTables);
        eventPublisher.publishEvent(orderTableGroupEvent);
    }
}
