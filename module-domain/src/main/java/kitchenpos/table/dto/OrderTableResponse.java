package kitchenpos.table.dto;

import kitchenpos.table.OrderTable;

public class OrderTableResponse {
    private Long id;
    private Long tableGroupId;
    private int numberOfGuests;
    private boolean empty;

    public OrderTableResponse() {
    }

    public OrderTableResponse(final OrderTable orderTable) {
        this.id = orderTable.getId();
        this.tableGroupId = orderTable.getTableGroupId();
        this.numberOfGuests = orderTable.getNumberOfGuests();
        this.empty = orderTable.isEmpty();
    }

    public OrderTableResponse(final Long id, final Long tableGroupId, final int numberOfGuests, final boolean empty) {
        this.id = id;
        this.tableGroupId = tableGroupId;
        this.numberOfGuests = numberOfGuests;
        this.empty = empty;
    }

    public Long getId() {
        return id;
    }

    public OrderTableResponse setId(final Long id) {
        this.id = id;
        return this;
    }

    public Long getTableGroupId() {
        return tableGroupId;
    }

    public OrderTableResponse setTableGroupId(final Long tableGroupId) {
        this.tableGroupId = tableGroupId;
        return this;
    }

    public int getNumberOfGuests() {
        return numberOfGuests;
    }

    public OrderTableResponse setNumberOfGuests(final int numberOfGuests) {
        this.numberOfGuests = numberOfGuests;
        return this;
    }

    public boolean isEmpty() {
        return empty;
    }

    public OrderTableResponse setEmpty(final boolean empty) {
        this.empty = empty;
        return this;
    }
}
