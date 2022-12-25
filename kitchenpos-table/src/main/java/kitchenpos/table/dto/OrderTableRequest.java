package kitchenpos.table.dto;

public class OrderTableRequest {

    private int numberOfGuests;
    private Boolean empty;

    public OrderTableRequest(int numberOfGuests, Boolean empty) {
        this.numberOfGuests = numberOfGuests;
        this.empty = empty;
    }

    public int getNumberOfGuests() {
        return numberOfGuests;
    }

    public Boolean getEmpty() {
        return empty;
    }
}
