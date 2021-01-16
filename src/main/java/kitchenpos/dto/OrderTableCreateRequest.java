package kitchenpos.dto;

import kitchenpos.domain.OrderTable;

public class OrderTableCreateRequest {

    private Long tableGroupId;

    private int numberOfGuests;

    private boolean empty;

    public Long getTableGroupId() {
        return tableGroupId;
    }

    public int getNumberOfGuests() {
        return numberOfGuests;
    }

    public boolean isEmpty() {
        return empty;
    }

    protected OrderTableCreateRequest() {
    }

    public OrderTableCreateRequest(Long tableGroupId, int numberOfGuests, boolean empty) {
        this.tableGroupId = tableGroupId;
        this.numberOfGuests = numberOfGuests;
        this.empty = empty;
    }

    public OrderTable toEntity() {
        return new OrderTable(tableGroupId, numberOfGuests, empty);
    }

}
