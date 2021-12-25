package kitchenpos.order.event;

import kitchenpos.order.domain.OrderTable;
import kitchenpos.order.domain.OrderTableRepository;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionalEventListener;

import java.util.List;

@Component
public class OrderTableEventHandler {
    private final OrderTableRepository orderTableRepository;

    public OrderTableEventHandler(OrderTableRepository orderTableRepository) {
        this.orderTableRepository = orderTableRepository;
    }

    @TransactionalEventListener
    public void group(OrderTableGroupEvent event) {
        List<OrderTable> orderTables = event.getOrderTables();

        orderTables.forEach(orderTable -> {
                    orderTable.group(event.getTableGroupId());
                });

        orderTableRepository.saveAll(orderTables);
    }

    @TransactionalEventListener
    public void ungroup(OrderTableUngroupEvent event) {
        List<OrderTable> orderTables = orderTableRepository.findByTableGroupId(event.getTableGroupId());
        orderTables.forEach(OrderTable::ungroup);

        orderTableRepository.saveAll(orderTables);
    }

}
