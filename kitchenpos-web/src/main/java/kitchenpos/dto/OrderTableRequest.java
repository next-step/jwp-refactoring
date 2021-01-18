package kitchenpos.dto;

import kitchenpos.domain.model.OrderTable;

public class OrderTableRequest {

    private Long id;

    private Long tableGroupId;

    private int numberOfGuests;

    private boolean empty;

    public Long getId() {
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

    protected OrderTableRequest() {
    }

    public OrderTableRequest(Long id, Long tableGroupId, int numberOfGuests, boolean empty) {
        this.id = id;
        this.tableGroupId = tableGroupId;
        this.numberOfGuests = numberOfGuests;
        this.empty = empty;
    }

    public static OrderTableRequest of(OrderTable orderTable) {
        return new OrderTableRequest(orderTable.getId(), orderTable.getTableGroupId(), orderTable.getNumberOfGuests(), orderTable.isEmpty());
    }

    public OrderTable toEntity() {
        return new OrderTable(tableGroupId, numberOfGuests, empty);
    }

}
