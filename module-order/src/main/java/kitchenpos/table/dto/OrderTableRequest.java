package kitchenpos.table.dto;

import java.util.Objects;

public class OrderTableRequest {
    private int numberOfGuests;
    private boolean empty;

    public OrderTableRequest() {
    }

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
        return getNumberOfGuests() == that.getNumberOfGuests() && isEmpty() == that.isEmpty();
    }

    @Override
    public int hashCode() {
        return Objects.hash(getNumberOfGuests(), isEmpty());
    }
}
