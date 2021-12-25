package kitchenpos.tableGroup.application;

import kitchenpos.order.domain.OrderTable;
import kitchenpos.order.domain.OrderTableRepository;
import kitchenpos.tableGroup.event.OrderTableGroupEvent;
import kitchenpos.tableGroup.event.OrderTableUngroupEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class OrderTableEventHandler {
    private final OrderTableRepository orderTableRepository;

    public OrderTableEventHandler(OrderTableRepository orderTableRepository) {
        this.orderTableRepository = orderTableRepository;
    }

    @EventListener
    public void group(OrderTableGroupEvent event) {
        List<OrderTable> orderTables = event.getOrderTables();

        orderTables.stream()
                .forEach(orderTable -> {
                    orderTable.group(event.getTableGroupId());
                });

        orderTableRepository.saveAll(orderTables);
    }

    @EventListener
    public void ungroup(OrderTableUngroupEvent event) {

        //TODO
    }


}
