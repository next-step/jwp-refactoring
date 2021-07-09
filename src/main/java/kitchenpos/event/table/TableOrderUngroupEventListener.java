package kitchenpos.event.table;

import kitchenpos.application.order.OrderService;
import kitchenpos.domain.table.OrderTable;
import kitchenpos.exception.order.InvalidOrderStatusException;
import org.apache.logging.log4j.util.Strings;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class TableOrderUngroupEventListener implements ApplicationListener<TableOrderUngroupEvent> {

    private final OrderService orderService;

    public TableOrderUngroupEventListener(OrderService orderService) {
        this.orderService = orderService;
    }

    @Override
    public void onApplicationEvent(TableOrderUngroupEvent event) {
        List<OrderTable> orderTables = event.getOrderTables();

        List<Long> ids = orderTables
                .stream()
                .map(OrderTable::getId)
                .collect(Collectors.toList());

        if (!orderService.isOrderCompletionByOrderTableIds(ids)) {
            throw new InvalidOrderStatusException("orderTableIds: " + Strings.join(ids, ','));
        }
    }
}
