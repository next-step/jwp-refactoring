package kitchenpos.dto;

import kitchenpos.domain.OrderTable;

public class TableRequest {

    private Long id;

    private int numberOfGuests;

    private boolean empty;

    public OrderTable toOrderTable() {
        return new OrderTable(null, numberOfGuests, empty);
    }

    public TableRequest() {
    }

    public TableRequest(Long id) {
        this.id = id;
    }

    public TableRequest(int numberOfGuests, boolean empty) {
        this.numberOfGuests = numberOfGuests;
        this.empty = empty;
    }

    public int getNumberOfGuests() {
        return numberOfGuests;
    }

    public boolean isEmpty() {
        return empty;
    }

    public Long getId() {
        return id;
    }
}
