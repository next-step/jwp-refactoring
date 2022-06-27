package kitchenpos.table.application.handler;

import java.util.List;
import kitchenpos.table.application.TableGroupValidator;
import kitchenpos.table.domain.OrderTable;
import kitchenpos.table.domain.OrderTableRepository;
import kitchenpos.table.domain.event.TableGroupingEvent;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.event.TransactionalEventListener;

@Transactional(readOnly = true)
@Component
public class OrderTableGroupingEventHandler {

    private final OrderTableRepository orderTableRepository;
    private final TableGroupValidator tableGroupValidator;

    public OrderTableGroupingEventHandler(OrderTableRepository orderTableRepository,
                                          TableGroupValidator tableGroupValidator) {
        this.orderTableRepository = orderTableRepository;
        this.tableGroupValidator = tableGroupValidator;
    }

    @TransactionalEventListener
    @Transactional
    public void handle(TableGroupingEvent event) {
        List<OrderTable> orderTables = orderTableRepository.findAllByIdIn(event.getOrderTableIds());
        tableGroupValidator.validateGrouping(orderTables);

        orderTables.forEach(orderTable -> orderTable.mappedByTableGroup(event.getTableGroup().getId()));
    }
}
