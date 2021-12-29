package kitchenpos.table.domain;

import java.util.List;

import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import kitchenpos.tablegroup.domain.UngroupingTableEvent;

@Component
public class UngroupingTableEventHandler {
    private final OrderTableRepository orderTableRepository;

    public UngroupingTableEventHandler(OrderTableRepository orderTableRepository) {
        this.orderTableRepository = orderTableRepository;
    }

    @Transactional
    @EventListener
    public void handle(UngroupingTableEvent event) {
        List<OrderTable> tables = orderTableRepository.findAllByTableGroupId(event.getTableGroupId());

        OrderTables orderTables = new OrderTables(tables);

        orderTables.unGroup();
        for (OrderTable orderTable : orderTables.getOrderTables()) {
            orderTableRepository.save(orderTable);
        }
    }
}
