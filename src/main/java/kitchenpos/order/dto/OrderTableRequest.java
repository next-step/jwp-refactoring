package kitchenpos.order.dto;

import kitchenpos.table.domain.OrderTableV2;

public class OrderTableRequest {
    private Integer numberOfGuests;
    private Boolean empty;

    public OrderTableRequest() {
    }

    public OrderTableRequest(Integer numberOfGuests, Boolean empty) {
        this.numberOfGuests = numberOfGuests;
        this.empty = empty;
    }

    public int getNumberOfGuests() {
        return numberOfGuests;
    }

    public boolean isEmpty() {
        return empty;
    }

    public OrderTableV2 toOrderTable() {
        return new OrderTableV2(null, null, numberOfGuests, empty);
    }
}
