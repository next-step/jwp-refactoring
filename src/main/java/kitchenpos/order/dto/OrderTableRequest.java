package kitchenpos.order.dto;

import kitchenpos.order.domain.OrderTable;

public class OrderTableRequest {
    private int numberOfGuests;
    private boolean empty;

    private OrderTableRequest(){

    }
    public OrderTableRequest(int numberOfGuests, boolean empty) {
        this.numberOfGuests = numberOfGuests;
        this.empty = empty;
    }
    public OrderTableRequest(int numberOfGuests) {
        this.numberOfGuests = numberOfGuests;
    }
    public OrderTableRequest(boolean empty) {
        this.empty = empty;
    }

    public OrderTable toOrderTable(){
        OrderTable orderTable = new OrderTable();
        orderTable.setEmpty(empty);
        orderTable.setNumberOfGuests(numberOfGuests);
        return orderTable;
    }

    public int getNumberOfGuests() {
        return numberOfGuests;
    }

    public boolean isEmpty() {
        return empty;
    }
}
