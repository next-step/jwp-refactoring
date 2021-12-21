package kitchenpos.application.tablegroup;

import org.springframework.stereotype.Component;

import kitchenpos.application.order.OrderService;
import kitchenpos.application.table.TableService;
import kitchenpos.domain.table.OrderTables;
import kitchenpos.exception.order.HasNotCompletionOrderException;

@Component
public class TableGroupValidator {
    private final OrderService orderService;
    private final TableService tableService;

    public TableGroupValidator(
        final OrderService orderService,
        final TableService tableService
    ) {
        this.orderService = orderService;
        this.tableService = tableService;
    }

    public void validateForUnGroup(OrderTables orderTables) {
        if (orderService.hasNotComplateStatus(orderTables.getOrderTableIds())) {
            throw new HasNotCompletionOrderException();
        }
    }

    public OrderTables getComplateOrderTable(Long tableGroupId) {
        final OrderTables orderTables = OrderTables.of(tableService.findByTableGroupId(tableGroupId));

        this.validateForUnGroup(orderTables);

        return orderTables;
    }
}
