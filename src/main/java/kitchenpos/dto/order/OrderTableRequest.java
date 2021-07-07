package kitchenpos.dto.order;

import kitchenpos.domain.order.OrderTable;

import java.util.Objects;

public class OrderTableRequest {

    private final int numberOfGuests;
    private final boolean empty;

    public OrderTableRequest(int numberOfGuests, boolean empty) {
        this.numberOfGuests = numberOfGuests;
        this.empty = empty;
    }

    public int getNumberOfGuests() {
        return numberOfGuests;
    }

    public boolean isEmpty() {
        return empty;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        OrderTableRequest that = (OrderTableRequest) o;
        return numberOfGuests == that.numberOfGuests && empty == that.empty;
    }

    @Override
    public int hashCode() {
        return Objects.hash(numberOfGuests, empty);
    }

    @Override
    public String toString() {
        return "OrderTableRequest{" +
                "numberOfGuests=" + numberOfGuests +
                ", empty=" + empty +
                '}';
    }

    public OrderTable toEntity() {
        return OrderTable.of(numberOfGuests, empty);
    }
}
