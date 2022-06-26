package kitchenpos.order.dto;

import kitchenpos.table.domain.GuestNumber;
import kitchenpos.table.domain.OrderTable;

public class OrderTableRequest {
    private Integer numberOfGuests;
    private Boolean empty;

    public OrderTableRequest() {
    }

    public OrderTableRequest(Integer numberOfGuests, Boolean empty) {
        this.numberOfGuests = numberOfGuests;
        this.empty = empty;
    }

    public Integer getNumberOfGuests() {
        return numberOfGuests;
    }

    public Boolean getEmpty() {
        return empty;
    }

    public boolean isEmpty() {
        return empty;
    }

    public OrderTable toOrderTable() {
        return new OrderTable(null, null, GuestNumber.of(numberOfGuests), empty);
    }
}
