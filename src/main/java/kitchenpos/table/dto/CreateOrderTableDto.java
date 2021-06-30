package kitchenpos.table.dto;

public class CreateOrderTableDto {

    private int numberOfGuests;
    private boolean empty;

    public CreateOrderTableDto() { }

    public CreateOrderTableDto(int numberOfGuests, boolean empty) {
        this.numberOfGuests = numberOfGuests;
        this.empty = empty;
    }

    public CreateOrderTableDto(int numberOfGuests) {
        this(numberOfGuests, false);
    }

    public CreateOrderTableDto(boolean empty) {
        this(0, empty);
    }

    public int getNumberOfGuests() {
        return numberOfGuests;
    }

    public boolean isEmpty() {
        return empty;
    }
}
