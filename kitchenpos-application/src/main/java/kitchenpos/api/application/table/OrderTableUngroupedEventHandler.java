package kitchenpos.api.application.table;

import java.util.List;

import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import kitchenpos.common.domain.table.OrderTable;
import kitchenpos.common.domain.table.OrderTableRepository;
import kitchenpos.common.domain.table.OrderTableValidator;
import kitchenpos.common.domain.tablegroup.event.TableGroupUngroupedEvent;

@Component
public class OrderTableUngroupedEventHandler {

    private final OrderTableRepository orderTableRepository;
    private final OrderTableValidator orderTableValidator;

    public OrderTableUngroupedEventHandler(OrderTableRepository orderTableRepository,
                                         OrderTableValidator orderTableValidator) {
        this.orderTableRepository = orderTableRepository;
        this.orderTableValidator = orderTableValidator;
    }

    @EventListener
    @Transactional
    public void handle(TableGroupUngroupedEvent event) {
        List<OrderTable> orderTables = orderTableRepository.findAllByTableGroupId(event.getTableGroupId());
        orderTableValidator.validateNotCompletionOrder(orderTables);

        orderTables.forEach(OrderTable::ungroup);
    }
}
