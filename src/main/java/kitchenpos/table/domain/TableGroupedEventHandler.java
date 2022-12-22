package kitchenpos.table.domain;

import java.util.List;
import java.util.stream.Collectors;

import javax.transaction.Transactional;

import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import kitchenpos.exception.EntityNotFoundException;
import kitchenpos.tablegroup.domain.TableGroupedEvent;

@Component
public class TableGroupedEventHandler {
    private final OrderTableRepository orderTableRepository;

    public TableGroupedEventHandler(OrderTableRepository orderTableRepository) {
        this.orderTableRepository = orderTableRepository;
    }

    @EventListener
    @Transactional
    public void handle(TableGroupedEvent event) {
        List<OrderTable> orderTables = event.getOrderTableIds()
            .stream()
            .map(this::findOrderTableById)
            .collect(Collectors.toList());
        OrderTables.of(orderTables).group(event.getTableGroupId());
    }

    private OrderTable findOrderTableById(Long orderTableId) {
        return orderTableRepository.findById(orderTableId)
            .orElseThrow(() -> new EntityNotFoundException(OrderTable.ENTITY_NAME, orderTableId));
    }

}
