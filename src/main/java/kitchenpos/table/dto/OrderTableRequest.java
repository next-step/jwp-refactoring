package kitchenpos.table.dto;

import kitchenpos.order.domain.OrderTable;

public class OrderTableRequest {

    private Long id;
    private Long tableGroupId;
    private int numberOfGuests;
    private boolean empty;

    public OrderTableRequest() {
    }

    public OrderTableRequest(Long id) {
        this(id, null, 0, false);
    }

    public OrderTableRequest(int numberOfGuest) {
        this(null, null, numberOfGuest, false);
    }

    public OrderTableRequest(boolean empty) {
        this(null, null, 0, empty);
    }

    public OrderTableRequest(Long tableGroupId, int numberOfGuest, boolean empty) {
        this(null, tableGroupId, numberOfGuest, empty);
    }

    public OrderTableRequest(Long id, Long tableGroupId, int numberOfGuests, boolean empty) {
        this.id = id;
        this.tableGroupId = tableGroupId;
        this.numberOfGuests = numberOfGuests;
        this.empty = empty;
    }

    public OrderTable toOrderTable() {
        return new OrderTable(
            id,
            tableGroupId,
            numberOfGuests,
            empty
        );
    }

    public Long getId() {
        return id;
    }

    public void setId(final Long id) {
        this.id = id;
    }

    public Long getTableGroupId() {
        return tableGroupId;
    }

    public void setTableGroupId(final Long tableGroupId) {
        this.tableGroupId = tableGroupId;
    }

    public int getNumberOfGuests() {
        return numberOfGuests;
    }

    public void setNumberOfGuests(final int numberOfGuests) {
        this.numberOfGuests = numberOfGuests;
    }

    public boolean isEmpty() {
        return empty;
    }

    public void setEmpty(final boolean empty) {
        this.empty = empty;
    }
}
