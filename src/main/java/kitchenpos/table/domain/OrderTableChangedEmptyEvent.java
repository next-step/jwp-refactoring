package kitchenpos.table.domain;

public class OrderTableChangedEmptyEvent {

    private OrderTable orderTable;

    public Long getOrderTableId() {
        return orderTable.getId();
    }

}
