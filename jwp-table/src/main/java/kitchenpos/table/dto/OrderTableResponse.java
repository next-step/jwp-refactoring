package kitchenpos.table.dto;

import kitchenpos.table.domain.OrderTable;

public class OrderTableResponse {
    private Long id;
    private int numberOfGuests;
    private boolean empty;

    protected OrderTableResponse() {
    }

    public OrderTableResponse(final Long id, final int numberOfGuests, final boolean empty) {
        this.id = id;
        this.numberOfGuests = numberOfGuests;
        this.empty = empty;
    }

    public static OrderTableResponse from(final OrderTable orderTable) {
        return new OrderTableResponse(orderTable.getId(), orderTable.getNumberOfGuests().toInt(), orderTable.getEmpty().isEmpty());
    }

    public static OrderTableResponse of(final Long id, final int numberOfGuestsm, final boolean empty) {
        return new OrderTableResponse(id, numberOfGuestsm, empty);
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
