package kitchenpos.table.domain;

public class TableChangeEmptyEvent {

    private Long orderTableId;

    public TableChangeEmptyEvent(Long orderTableId) {
        this.orderTableId = orderTableId;
    }

    public Long getOrderTableId() {
        return orderTableId;
    }

}
