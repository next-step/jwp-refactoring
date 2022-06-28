package kitchenpos.ordertable.dto;

import java.util.Objects;
import kitchenpos.domain.NumberOfGuests;
import kitchenpos.ordertable.domain.OrderTable;

public class OrderTableResponse {
    private final Long id;
    private final int numberOfGuests;
    private final boolean empty;

    private OrderTableResponse(Long id, NumberOfGuests numberOfGuests, boolean empty) {
        this.id = id;
        this.numberOfGuests = numberOfGuests.value();
        this.empty = empty;
    }

    public static OrderTableResponse from(OrderTable orderTable) {
        return new OrderTableResponse(orderTable.id(), orderTable.numberOfGuests(), orderTable.isEmpty());
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
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        OrderTableResponse that = (OrderTableResponse) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
