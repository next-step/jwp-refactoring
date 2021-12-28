package kitchenpos.ordertable.domain.event;

import java.util.List;
import kitchenpos.ordertable.domain.OrderTable;
import kitchenpos.ordertable.domain.OrderTableRepository;
import kitchenpos.tablegroup.domain.event.TableGroupingEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionalEventListener;

@Component
public class TableGroupingEventListener implements ApplicationListener<TableGroupingEvent> {

    Logger logger = LoggerFactory.getLogger(TableGroupingEventListener.class);
    private final OrderTableRepository orderTableRepository;

    public TableGroupingEventListener(final OrderTableRepository orderTableRepository) {
        this.orderTableRepository = orderTableRepository;
    }

    @TransactionalEventListener
    public void onApplicationEvent(TableGroupingEvent event) {
        logger.warn("onApplicationEvent");
        List<OrderTable> orderTables = orderTableRepository.findAllById(event.getOrderTableIds());
        orderTables.forEach(orderTable -> orderTable.group(event.getTableGroupId()));

        orderTableRepository.saveAll(orderTables);
    }
}
