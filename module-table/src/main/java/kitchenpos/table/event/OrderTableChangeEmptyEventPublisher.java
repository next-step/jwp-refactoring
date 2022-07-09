package kitchenpos.table.event;

import kitchenpos.table.domain.OrderTable;

public class OrderTableChangeEmptyEventPublisher {
    private final OrderTable orderTable;

    public OrderTableChangeEmptyEventPublisher(OrderTable orderTable) {
        this.orderTable = orderTable;
    }

    public OrderTable getOrderTable() {
        return orderTable;
    }
}
