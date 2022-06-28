package kitchenpos.ui.dto;

import kitchenpos.domain.OrderTable;

public class OrderTableCreateRequest {
    private Long id;
    private int numberOfGuests;
    private boolean empty;

    public OrderTable toEntity() {
        return new OrderTable(id, null, numberOfGuests, empty);
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
