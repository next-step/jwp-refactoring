package kitchenpos.table.dto;

import kitchenpos.table.domain.OrderTable;

public class OrderTableResponse {
    private final long id;
    private final Long tableGroupId;
    private final int numberOfGuests;
    private final boolean empty;

    private OrderTableResponse(long id, Long tableGroupId, int numberOfGuests, boolean empty) {
        this.id = id;
        this.tableGroupId = tableGroupId;
        this.numberOfGuests = numberOfGuests;
        this.empty = empty;
    }

    public static OrderTableResponse from(OrderTable orderTable) {
        return new OrderTableResponse(orderTable.id(), orderTable.tableGroupId(), orderTable.numberOfGuests(), orderTable.isEmpty());
    }

    public long getId() {
        return id;
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
