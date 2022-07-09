package kitchenpos.orderTable.dto;

import kitchenpos.orderTable.domain.OrderTable;

public class OrderTableResponse {

    private Long orderTableId;
    private Long tableGroupId;
    private int numberOfGuests;
    private boolean empty;

    public OrderTableResponse() {
    }

    public OrderTableResponse(OrderTable orderTable) {
        this.orderTableId = orderTable.getId();
        this.numberOfGuests = orderTable.getNumberOfGuests();
        this.empty = orderTable.isEmpty();
        this.tableGroupId = orderTable.getTableGroupId();
    }

    public Long getOrderTableId() {
        return orderTableId;
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
}
