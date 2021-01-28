package kitchenpos.dto.table;

import kitchenpos.domain.table.GuestNumber;
import kitchenpos.domain.table.OrderTable;
import kitchenpos.domain.table.OrderTableGroup;

public class TableRequest {
    private Long tableId;
    private int guestNumber;
    private Boolean empty;

    public TableRequest() {}

    public TableRequest(int guestNumber, Boolean empty) {
        this.guestNumber = guestNumber;
        this.empty = empty;
    }

    public TableRequest(int guestNumber) {
        this.guestNumber = guestNumber;
    }

    public TableRequest(Boolean empty) {
        this.empty = empty;
    }

    public TableRequest(Long tableId) {
        this.tableId = tableId;
    }

    public Long getTableId() {
        return tableId;
    }

    public GuestNumber getGuestNumber() {
        return new GuestNumber(guestNumber);
    }

    public Boolean isEmpty() {
        return empty;
    }

    public OrderTable toOrderTable(OrderTableGroup orderTableGroup) {
        return new OrderTable(orderTableGroup, new GuestNumber(guestNumber), empty);
    }
}
