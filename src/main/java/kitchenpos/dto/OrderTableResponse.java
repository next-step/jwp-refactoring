package kitchenpos.dto;

import kitchenpos.domain.OrderTable;

import java.util.Objects;

public class OrderTableResponse {
    Long id;
    int numberOfGuests;
    boolean empty;

    protected OrderTableResponse() {
    }

    private OrderTableResponse(Long id, int numberOfGuests, boolean empty) {
        this.id = id;
        this.numberOfGuests = numberOfGuests;
        this.empty = empty;
    }

    public static OrderTableResponse from(OrderTable orderTable) {
        return new OrderTableResponse(orderTable.getId(), orderTable.getNumberOfGuests(), orderTable.isEmpty());
    }

    public Long getId() {
        return id;
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
        OrderTableResponse that = (OrderTableResponse) o;
        return getNumberOfGuests() == that.getNumberOfGuests()
                && isEmpty() == that.isEmpty()
                && Objects.equals(getId(), that.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId(), getNumberOfGuests(), isEmpty());
    }
}
