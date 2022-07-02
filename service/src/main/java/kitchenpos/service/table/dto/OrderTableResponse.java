package kitchenpos.service.table.dto;

import kitchenpos.domain.table.OrderTable;

public class OrderTableResponse {
    private Long id;
    private int numberOfGuests;
    private boolean isEmpty;

    public OrderTableResponse(OrderTable orderTable) {
        this.id = orderTable.getId();
        this.numberOfGuests = orderTable.getNumberOfGuests();
        this.isEmpty = orderTable.isEmpty();
    }

    public OrderTableResponse() {
    }

    public Long getId() {
        return id;
    }

    public int getNumberOfGuests() {
        return numberOfGuests;
    }

    public boolean isEmpty() {
        return isEmpty;
    }
}
