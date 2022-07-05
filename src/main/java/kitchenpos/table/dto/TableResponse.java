package kitchenpos.table.dto;

import kitchenpos.table.domain.OrderTable;

public class TableResponse {
    private Long id;
    private Long tableGroupId;
    private int numberOfGuests;
    private boolean empty;

    public TableResponse(OrderTable orderTable) {
        id = orderTable.getId();
        if (orderTable.getTableGroup() != null) {
            tableGroupId = orderTable.getTableGroup().getId();
        }
        numberOfGuests = orderTable.getNumberOfGuests();
        empty = orderTable.isEmpty();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getTableGroupId() {
        return tableGroupId;
    }

    public void setTableGroupId(Long tableGroupId) {
        this.tableGroupId = tableGroupId;
    }

    public int getNumberOfGuests() {
        return numberOfGuests;
    }

    public void setNumberOfGuests(int numberOfGuests) {
        this.numberOfGuests = numberOfGuests;
    }

    public boolean isEmpty() {
        return empty;
    }

    public void setEmpty(boolean empty) {
        this.empty = empty;
    }
}
