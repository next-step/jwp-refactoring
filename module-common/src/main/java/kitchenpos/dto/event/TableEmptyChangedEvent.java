package kitchenpos.dto.event;

public class TableEmptyChangedEvent {

    private final Long orderTableId;

    public TableEmptyChangedEvent(Long orderTableId) {
        this.orderTableId = orderTableId;
    }

    public Long getOrderTableId() {
        return orderTableId;
    }
}

