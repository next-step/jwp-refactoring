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

    public static OrderTableResponse of(OrderTable orderTable) {
        return new OrderTableResponse(
                orderTable.getId(),
                orderTable.getNumberOfGuests(),
                orderTable.isEmpty());
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
        if (!(o instanceof OrderTableResponse)) return false;
        OrderTableResponse that = (OrderTableResponse) o;
        if (Objects.equals(getId(), that.getId())) return true;
        return getNumberOfGuests() == that.getNumberOfGuests() && isEmpty() == that.isEmpty();
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId(), getNumberOfGuests(), isEmpty());
    }
}
