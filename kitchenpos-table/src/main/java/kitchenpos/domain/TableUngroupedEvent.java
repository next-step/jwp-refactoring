package kitchenpos.domain;

public class TableUngroupedEvent {

    private Long orderTableId;

    public TableUngroupedEvent(Long orderTableId) {
        this.orderTableId = orderTableId;
    }

    public Long getOrderTableId() {
        return orderTableId;
    }

}
