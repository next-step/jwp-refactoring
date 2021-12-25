package kitchenpos.table.dto;

import kitchenpos.table.OrderTable;

public class OrderTableRequest {

    private final Integer numberOfGuests;
    private final Boolean empty;

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

    public OrderTable toEntity() {
        return OrderTable.of(numberOfGuests, empty);
    }
}
