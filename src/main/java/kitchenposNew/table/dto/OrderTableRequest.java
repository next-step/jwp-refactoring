package kitchenposNew.table.dto;

import kitchenposNew.table.domain.OrderTable;

public class OrderTableRequest {
    private int numberOfGuests;

    protected OrderTableRequest() {
    }

    public OrderTableRequest(int numberOfGuests) {
        this.numberOfGuests = numberOfGuests;
    }

    public OrderTable toOrderTable(){
        return new OrderTable(numberOfGuests);
    }

    public int getNumberOfGuests() {
        return this.numberOfGuests;
    }
}
