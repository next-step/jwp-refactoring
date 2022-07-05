package kitchenpos.table.dto;

import kitchenpos.table.domain.OrderTable;

import java.util.Objects;

public class CreateOrderTableRequest {
    private final int numberOfGuests;
    private final boolean empty;

    public CreateOrderTableRequest(final int numberOfGuests, final boolean empty) {
        this.numberOfGuests = numberOfGuests;
        this.empty = empty;
    }

    public int getNumberOfGuests() {
        return numberOfGuests;
    }

    public boolean isEmpty() {
        return empty;
    }

    public OrderTable toEntity() {
        return OrderTable.of(numberOfGuests, empty);
    }

    @Override
    public String toString() {
        return "CreateOrderTableRequest{" +
                "numberOfGuests=" + numberOfGuests +
                ", empty=" + empty +
                '}';
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        final CreateOrderTableRequest that = (CreateOrderTableRequest) o;
        return numberOfGuests == that.numberOfGuests && empty == that.empty;
    }

    @Override
    public int hashCode() {
        return Objects.hash(numberOfGuests, empty);
    }
}
