package kitchenpos.table.dto;

import kitchenpos.table.domain.OrderTable;

public class OrderTableRequest {

    private Long tableGroupId;
    private int numberOfGuests;
    private boolean empty;

    public OrderTableRequest() {
    }

    public OrderTableRequest(final int numberOfGuests, final boolean empty) {
        this(null, numberOfGuests, empty);
    }

    public OrderTableRequest(final Long tableGroupId, final int numberOfGuests,
        final boolean empty) {
        this.tableGroupId = tableGroupId;
        this.numberOfGuests = numberOfGuests;
        this.empty = empty;
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

    public OrderTable toOrderTable() {
        return new OrderTable(numberOfGuests, empty);
    }
}
