package kitchenpos.application.ordertable.dto;

import kitchenpos.ordertable.domain.OrderTable;

import java.util.Objects;

public class OrderTableResponse {
    private final Long id;
    private final int numberOfGuests;
    private final boolean empty;

    private OrderTableResponse(Long id, int numberOfGuests, boolean empty) {
        this.id = id;
        this.numberOfGuests = numberOfGuests;
        this.empty = empty;
    }

    public static OrderTableResponse of(OrderTable savedOrderTable) {
        return new OrderTableResponse(savedOrderTable.getId(), savedOrderTable.getNumberOfGuests(), savedOrderTable.isEmpty());
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
    public boolean equals(Object target) {
        if (this == target) return true;
        if (target == null || getClass() != target.getClass()) return false;

        OrderTableResponse that = (OrderTableResponse) target;

        if (numberOfGuests != that.numberOfGuests) return false;
        if (empty != that.empty) return false;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, numberOfGuests, empty);
    }
}
