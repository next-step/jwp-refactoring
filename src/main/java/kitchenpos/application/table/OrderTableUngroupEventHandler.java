package kitchenpos.application.table;

import java.util.List;
import kitchenpos.application.tablegroup.TableGroupValidator;
import kitchenpos.domain.table.OrderTable;
import kitchenpos.domain.table.OrderTableRepository;
import kitchenpos.domain.tablegroup.event.TableUngroupEvent;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
public class OrderTableUngroupEventHandler {

    private final OrderTableRepository orderTableRepository;
    private final TableGroupValidator tableGroupValidator;

    public OrderTableUngroupEventHandler(OrderTableRepository orderTableRepository,
                                         TableGroupValidator tableGroupValidator) {
        this.orderTableRepository = orderTableRepository;
        this.tableGroupValidator = tableGroupValidator;
    }

    @Async
    @EventListener
    @Transactional
    public void handle(TableUngroupEvent event) {
        List<OrderTable> orderTables = orderTableRepository.findAllByTableGroupId(event.getTableGroup().getId());
        tableGroupValidator.validateUngroup(orderTables);

        orderTables.forEach(OrderTable::ungroup);
    }
}
