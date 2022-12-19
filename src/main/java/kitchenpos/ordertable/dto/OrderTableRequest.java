package kitchenpos.ordertable.dto;

import kitchenpos.ordertable.domain.OrderTable;

public class OrderTableRequest {

    private Long tableGroupId;
    private int numberOfGuests;
    private boolean empty;

    public static OrderTableRequest from(OrderTable orderTable) {
        return new OrderTableRequest(orderTable.getTableGroupId()
                , orderTable.getNumberOfGuestsValue()
                , orderTable.isEmpty());
    }

    public OrderTableRequest() {}

    public OrderTableRequest(Long tableGroupId, int numberOfGuests, boolean empty) {
        this.tableGroupId = tableGroupId;
        this.numberOfGuests = numberOfGuests;
        this.empty = empty;
    }

    public OrderTable toOrderTable() {
        return new OrderTable(tableGroupId, numberOfGuests, empty);
    }

    public Long getTableGroupId() {
        return tableGroupId;
    }

    public int getNumberOfGuests() {
        return numberOfGuests;
    }

    public boolean isEmpty() {
        return empty;
    }

    public void setEmpty(boolean empty) {
        this.empty = empty;
    }

    public void updateNumberOfGuests(int i) {
        numberOfGuests = i;
    }
}
