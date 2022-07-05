package kitchenpos.table.dto;

import kitchenpos.table.domain.OrderTable;

public class TableGroupOrderTableResponse {
    private Long id;
    private int numberOfGuests;
    private boolean empty;

    public TableGroupOrderTableResponse(OrderTable orderTable) {
        id = orderTable.getId();
        numberOfGuests = orderTable.getNumberOfGuests();
        empty = orderTable.isEmpty();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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
