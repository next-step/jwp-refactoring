package kitchenpos.order.application.event;

import kitchenpos.order.application.TableValidator;
import kitchenpos.order.domain.OrderTable;
import kitchenpos.order.repository.TableRepository;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class TableGroupingHandler {
    private final TableRepository tableRepository;
    private final TableValidator tableValidator;

    public TableGroupingHandler(final TableRepository tableRepository, final TableValidator tableValidator) {
        this.tableRepository = tableRepository;
        this.tableValidator = tableValidator;
    }

    @EventListener
    public void handle(TableGroupingEvent event) {
        List<OrderTable> orderTables = tableRepository.findAllById(event.getTableIds());
        for (OrderTable orderTable : orderTables) {
            tableValidator.validateTableGroup(orderTable);
            orderTable.group(event.getTableGroupId());
        }
    }

    @EventListener
    public void handle(TableUnGroupingEvent event) {
        List<OrderTable> orderTables = tableRepository.findAllById(event.getTableIds());
        orderTables.forEach(OrderTable::unGroup);
    }
}
