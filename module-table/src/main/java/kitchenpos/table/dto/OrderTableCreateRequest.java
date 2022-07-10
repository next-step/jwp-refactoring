package kitchenpos.table.dto;

import kitchenpos.table.domain.NumberOfGuests;
import kitchenpos.table.domain.OrderTable;

public class OrderTableCreateRequest {
    private int numberOfGuests;

    protected OrderTableCreateRequest() {}

    public OrderTableCreateRequest(int numberOfGuests) {
        this.numberOfGuests = numberOfGuests;
    }

    public OrderTable of() {
        return new OrderTable(new NumberOfGuests(numberOfGuests));
    }

    public int getNumberOfGuests() {
        return numberOfGuests;
    }
}
