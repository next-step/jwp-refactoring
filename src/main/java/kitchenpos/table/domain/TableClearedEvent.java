package kitchenpos.table.domain;

public class TableClearedEvent {
    private final OrderTable orderTable;

    public TableClearedEvent(final OrderTable orderTable) {
        this.orderTable = orderTable;
    }

    public Long getOrderTableId() {
        return orderTable.getId();
    }
}
