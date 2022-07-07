package kichenpos.table.ui.dto;

import kichenpos.table.domain.OrderTable;

public class OrderTableCreateRequest {
    private Long id;
    private int numberOfGuests;
    private boolean empty;

    private OrderTableCreateRequest() {
    }

    public OrderTableCreateRequest(Long id) {
        this.id = id;
    }

    public OrderTableCreateRequest(int numberOfGuests, boolean empty) {
        this.numberOfGuests = numberOfGuests;
        this.empty = empty;
    }

    public OrderTable toEntity() {
        return new OrderTable(id, numberOfGuests, empty);
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
