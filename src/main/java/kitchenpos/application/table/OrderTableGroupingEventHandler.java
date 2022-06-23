package kitchenpos.application.table;

import java.util.List;
import kitchenpos.application.tablegroup.TableGroupValidator;
import kitchenpos.domain.table.OrderTable;
import kitchenpos.domain.table.OrderTableRepository;
import kitchenpos.domain.tablegroup.event.TableGroupingEvent;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
public class OrderTableGroupingEventHandler {

    private final OrderTableRepository orderTableRepository;
    private final TableGroupValidator tableGroupValidator;

    public OrderTableGroupingEventHandler(OrderTableRepository orderTableRepository,
                                          TableGroupValidator tableGroupValidator) {
        this.orderTableRepository = orderTableRepository;
        this.tableGroupValidator = tableGroupValidator;
    }

    @Async
    @EventListener
    @Transactional
    public void handle(TableGroupingEvent event) {
        List<OrderTable> orderTables = orderTableRepository.findAllByIdIn(event.getOrderTableIds());
        tableGroupValidator.validateGrouping(orderTables);

        orderTables.forEach(orderTable -> orderTable.mappedByTableGroup(event.getTableGroup().getId()));
    }
}
