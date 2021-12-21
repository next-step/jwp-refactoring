package kitchenpos.application.tablegroup;

import org.springframework.stereotype.Component;

import kitchenpos.application.order.OrderService;
import kitchenpos.domain.table.OrderTables;
import kitchenpos.exception.order.HasNotCompletionOrderException;

@Component
public class TableGroupValidator {
    private final OrderService orderService;

    public TableGroupValidator(
        final OrderService orderService
    ) {
        this.orderService = orderService;
    }

    public void validateForUnGroup(OrderTables orderTables) {
        if (orderService.hasNotComplateStatus(orderTables.getOrderTableIds())) {
            throw new HasNotCompletionOrderException();
        }
    }
}
