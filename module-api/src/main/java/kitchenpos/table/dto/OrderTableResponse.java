package kitchenpos.table.dto;

import kitchenpos.table.domain.OrderTable;

import java.util.Objects;

public class OrderTableResponse {

    private final Long id;
    private final int numberOfGuests;
    private final boolean empty;

    public OrderTableResponse(Long id, int numberOfGuests, boolean empty) {
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
        return numberOfGuests == that.numberOfGuests && empty == that.empty && Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, numberOfGuests, empty);
    }
}
