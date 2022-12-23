package kitchenpos.table.dto;

import kitchenpos.table.domain.OrderTable;

public class OrderTableRequest {

    private Long tableGroupId;
    private int numberOfGuests;
    private boolean empty;

    public OrderTableRequest() {

    }

    public OrderTableRequest(Long tableGroupId, int numberOfGuests, boolean empty) {
        this.tableGroupId = tableGroupId;
        this.numberOfGuests = numberOfGuests;
        this.empty = empty;
    }

    public static OrderTableRequest of(OrderTable orderTable) {
        Long tableGroupId = null;

        if (orderTable.getTableGroup() != null) {
            tableGroupId = orderTable.getTableGroup().getId();
        }

        return new OrderTableRequest(tableGroupId, orderTable.getNumberOfGuests(), orderTable.isEmpty());
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
