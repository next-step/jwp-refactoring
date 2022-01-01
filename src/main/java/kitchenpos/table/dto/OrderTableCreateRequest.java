package kitchenpos.table.dto;

public class OrderTableCreateRequest {
    private Integer numberOfGuests;
    private boolean empty;

    public OrderTableCreateRequest(Integer numberOfGuests, boolean empty) {
        this.numberOfGuests = numberOfGuests;
        this.empty = empty;
    }

    public static OrderTableCreateRequest of(Integer numberOfGuests, boolean empty) {
        return new OrderTableCreateRequest(numberOfGuests, empty);
    }

    public Integer getNumberOfGuests() {
        return numberOfGuests;
    }

    public boolean isEmpty() {
        return empty;
    }
}
