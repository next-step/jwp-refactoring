package kitchenpos.order.domain;

import java.util.List;

import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import kitchenpos.order.repository.OrderTableRepository;

@Component
public class TableGroupSavedEventHandler {

    private final OrderTableRepository orderTableRepository;

    public TableGroupSavedEventHandler(OrderTableRepository orderTableRepository) {
        this.orderTableRepository = orderTableRepository;
    }

    @EventListener
    @Transactional
    public void handle(TableGroupSavedEvent event) {
        List<OrderTable> findOrderTables = orderTableRepository.findAllByIdIn(event.getOrderTableIds());
        for (OrderTable findOrderTable : findOrderTables) {
            findOrderTable.changeTableGroup(event.getTableGroupId());
        }
    }
}
