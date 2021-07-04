package kitchenpos.dto.order;

import kitchenpos.domain.order.OrderTable;

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
    public String toString() {
        return "OrderTableResponse2{" +
                "id=" + id +
                ", numberOfGuests=" + numberOfGuests +
                ", empty=" + empty +
                '}';
    }
}
