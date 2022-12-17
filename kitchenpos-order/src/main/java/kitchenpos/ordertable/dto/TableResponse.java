package kitchenpos.ordertable.dto;


import kitchenpos.ordertable.domain.OrderTable;

public class TableResponse {
    private Long id;
    private Long tableGroupId;
    private int numberOfGuests;
    private boolean empty;

    public TableResponse() {
    }

    public TableResponse(Long id, Long tableGroupId, int numberOfGuests, boolean empty) {
        this.id = id;
        this.tableGroupId = tableGroupId;
        this.numberOfGuests = numberOfGuests;
        this.empty = empty;
    }

    public static TableResponse from(OrderTable table) {
        return new TableResponse(table.getId(), table.getTableGroupId(), table.getNumberOfGuests(), table.isEmpty());
    }

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
}
