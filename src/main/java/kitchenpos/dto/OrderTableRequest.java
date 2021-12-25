package kitchenpos.dto;

import kitchenpos.domain.OrderTable;
import kitchenpos.domain.TableGroup;

public class OrderTableRequest {
    private int numberOfGuests;

    public OrderTableRequest() {
    }

    public OrderTableRequest(int numberOfGuests) {
        this.numberOfGuests = numberOfGuests;
    }

    public int getNumberOfGuests() {
        return numberOfGuests;
    }

    public OrderTable toOrderTable() {
        return new OrderTable(numberOfGuests);
    }
}
