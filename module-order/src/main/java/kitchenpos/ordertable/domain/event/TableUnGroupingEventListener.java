package kitchenpos.ordertable.domain.event;

import java.util.List;
import kitchenpos.ordertable.domain.OrderTable;
import kitchenpos.ordertable.domain.OrderTableRepository;
import kitchenpos.tablegroup.domain.event.TableUnGroupingEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionalEventListener;

@Component
public class TableUnGroupingEventListener implements ApplicationListener<TableUnGroupingEvent> {

    Logger logger = LoggerFactory.getLogger(TableUnGroupingEventListener.class);
    private final OrderTableRepository orderTableRepository;

    public TableUnGroupingEventListener(final OrderTableRepository orderTableRepository) {
        this.orderTableRepository = orderTableRepository;
    }

    @TransactionalEventListener
    public void onApplicationEvent(TableUnGroupingEvent event) {
        List<OrderTable> orderTables = orderTableRepository.findAllByTableGroupId(
            event.getTableGroupId());

        orderTables.forEach(OrderTable::ungroup);
        orderTableRepository.saveAll(orderTables);
    }
}
