package kitchenpos.ordertable.dto;

import kitchenpos.ordertable.domain.OrderTable;

public class OrderTableEmptyChangeResponse {
    private Long id;
    private int numberOfGuests;
    private boolean empty;

    public OrderTableEmptyChangeResponse() {
    }

    public OrderTableEmptyChangeResponse(Long id, int numberOfGuests, boolean empty) {
        this.id = id;
        this.numberOfGuests = numberOfGuests;
        this.empty = empty;
    }

    public static OrderTableEmptyChangeResponse of(OrderTable orderTable) {
        return new OrderTableEmptyChangeResponse(
                orderTable.getId(),
                orderTable.getNumberOfGuests(),
                orderTable.isEmpty()
        );
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
}
