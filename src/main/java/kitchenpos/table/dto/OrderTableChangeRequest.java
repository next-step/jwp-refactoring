package kitchenpos.table.dto;

public class OrderTableChangeRequest {
    private final Integer numberOfGuests;
    private final Boolean empty;

    public OrderTableChangeRequest(Integer numberOfGuests, Boolean empty) {
        this.numberOfGuests = numberOfGuests;
        this.empty = empty;
    }

    public Integer getNumberOfGuests() {
        return numberOfGuests;
    }

    public boolean isEmpty() {
        return empty;
    }
}
