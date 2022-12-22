package kitchenpos.table.domain;

public class TableEmptyChangedEvent {

    private Long orderTableId;

    public TableEmptyChangedEvent(Long orderTableId) {
        this.orderTableId = orderTableId;
    }

    public Long getOrderTableId() {
        return orderTableId;
    }
}
