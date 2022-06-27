package kitchenpos.table.application;

import kitchenpos.order.application.OrderService;
import kitchenpos.table.domain.OrderTable;
import org.springframework.stereotype.Component;

@Component
public class TableValidator {

    private final OrderService orderService;

    public TableValidator(OrderService orderService) {
        this.orderService = orderService;
    }

    public void changeEmpty(OrderTable orderTable) {
        validateExistTableGroup(orderTable);
        validateOrderTableStatus(orderTable);
    }

    private void validateOrderTableStatus(OrderTable orderTable) {
        if (orderService.isExistDontUnGroupState(orderTable.getId())) {
            throw new IllegalArgumentException();
        }
    }

    private void validateExistTableGroup(OrderTable orderTable) {
        if (orderTable.getTableGroupId() != null) {
            throw new IllegalArgumentException();
        }
    }
}
