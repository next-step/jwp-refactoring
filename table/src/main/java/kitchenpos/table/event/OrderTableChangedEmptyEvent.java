package kitchenpos.table.event;

import kitchenpos.table.domain.OrderTable;
import kitchenpos.table.dto.OrderTableRequest;

public class OrderTableChangedEmptyEvent {
    OrderTable orderTable;

    public OrderTableChangedEmptyEvent(OrderTable orderTable) {
        this.orderTable = orderTable;
    }

    public OrderTable getOrderTable() {
        return orderTable;
    }

    public Long getOrderTableId() {
        return orderTable.getId();
    }
}
