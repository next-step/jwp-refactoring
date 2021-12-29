package kitchenpos.table.domain;

public class OrderStatusValidateEvent {
    private final OrderTable orderTable;

    public OrderStatusValidateEvent(OrderTable orderTable) {
        this.orderTable = orderTable;
    }

    public OrderTable getOrderTable() {
        return orderTable;
    }
}
