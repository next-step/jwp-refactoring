package kitchenpos.table.event;

import kitchenpos.table.domain.OrderTable;

public class ChangeEmptyTableEventPublisher {
    private final OrderTable orderTable;

    public ChangeEmptyTableEventPublisher(OrderTable orderTable) {
        this.orderTable = orderTable;
    }

    public OrderTable orderTable() {
        return orderTable;
    }
}
