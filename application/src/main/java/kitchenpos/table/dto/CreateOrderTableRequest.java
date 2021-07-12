package kitchenpos.table.dto;

public class CreateOrderTableRequest {

    private int numberOfGuests;
    private boolean empty;

    public CreateOrderTableRequest() { }

    public CreateOrderTableRequest(int numberOfGuests, boolean empty) {
        this.numberOfGuests = numberOfGuests;
        this.empty = empty;
    }

    public CreateOrderTableRequest(int numberOfGuests) {
        this(numberOfGuests, false);
    }

    public CreateOrderTableRequest(boolean empty) {
        this(0, empty);
    }

    public CreateOrderTableDto toDomainDto() {
        return new CreateOrderTableDto(numberOfGuests, empty);
    }

    public int getNumberOfGuests() {
        return numberOfGuests;
    }

    public boolean isEmpty() {
        return empty;
    }
}
