package kitchenpos.table.publisher;

import kitchenpos.table.domain.OrderTable;
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

    public void groupEventPublish(TableGroup tableGroup, List<Long> orderTableIds) {
        OrderTableGroupEvent orderTableGroupEvent = new OrderTableGroupEvent(tableGroup, orderTableIds);
        eventPublisher.publishEvent(orderTableGroupEvent);
    }
}
