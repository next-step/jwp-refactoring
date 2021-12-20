package kitchenpos.api.application.table;

import java.util.List;

import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import kitchenpos.common.domain.table.OrderTable;
import kitchenpos.common.domain.table.OrderTableRepository;
import kitchenpos.common.domain.table.OrderTableValidator;
import kitchenpos.common.domain.tablegroup.event.TableGroupGroupedEvent;

@Component
public class OrderTableGroupedEventHandler {

    private final OrderTableRepository orderTableRepository;
    private final OrderTableValidator orderTableValidator;

    public OrderTableGroupedEventHandler(OrderTableRepository orderTableRepository,
                                         OrderTableValidator orderTableValidator) {
        this.orderTableRepository = orderTableRepository;
        this.orderTableValidator = orderTableValidator;
    }

    @Async
    @EventListener
    @Transactional
    public void handle(TableGroupGroupedEvent event) {
        List<OrderTable> orderTables = orderTableRepository.findAllByIdIn(event.getOrderTableIds());
        orderTableValidator.validateGroupOrderTables(event.getOrderTableIds(), orderTables);

        orderTables.forEach(orderTable -> orderTable.alignTableGroup(event.getTableGroupId()));
    }
}
