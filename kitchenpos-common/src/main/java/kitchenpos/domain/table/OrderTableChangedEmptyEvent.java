package kitchenpos.domain.table;

public class OrderTableChangedEmptyEvent {

    private OrderTable orderTable;

    public Long getOrderTableId() {
        return orderTable.getId();
    }

}
