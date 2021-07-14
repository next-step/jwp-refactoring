package kitchenpos.order.dto;

import kitchenpos.order.domain.OrderTable;

import java.util.Objects;

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

    public static TableResponse of(OrderTable orderTable) {
        return new TableResponse(orderTable.getId(), null, orderTable.getNumberOfGuests().value(), orderTable.isEmpty());
    }

    public static TableResponse of(OrderTable orderTable, Long tableGroupId) {
        return new TableResponse(orderTable.getId(), tableGroupId, orderTable.getNumberOfGuests().value(), orderTable.isEmpty());
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
