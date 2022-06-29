package kitchenpos.table.application.handler;

import java.util.List;
import kitchenpos.table.application.TableGroupValidator;
import kitchenpos.table.domain.OrderTable;
import kitchenpos.table.domain.OrderTableRepository;
import kitchenpos.table.domain.event.TableUngroupEvent;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.event.TransactionalEventListener;

@Transactional(readOnly = true)
@Component
public class OrderTableUngroupEventHandler {

    private final OrderTableRepository orderTableRepository;
    private final TableGroupValidator tableGroupValidator;

    public OrderTableUngroupEventHandler(OrderTableRepository orderTableRepository,
                                         TableGroupValidator tableGroupValidator) {
        this.orderTableRepository = orderTableRepository;
        this.tableGroupValidator = tableGroupValidator;
    }

    @TransactionalEventListener
    @Transactional
    public void handle(TableUngroupEvent event) {
        List<OrderTable> orderTables = orderTableRepository.findAllByTableGroupId(event.getTableGroup().getId());
        tableGroupValidator.validateUngroup(orderTables);

        orderTables.forEach(OrderTable::ungroup);
    }
}
