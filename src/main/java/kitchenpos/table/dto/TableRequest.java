package kitchenpos.table.dto;

import kitchenpos.table.domain.OrderTable;

public class TableRequest {
    private Long id;
    private int numberOfGuests;
    private boolean empty;

    public void setId(final Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }

    public void setNumberOfGuests(final int numberOfGuests) {
        this.numberOfGuests = numberOfGuests;
    }

    public void setEmpty(final boolean empty) {
        this.empty = empty;
    }

    public OrderTable toEntity() {
        return new OrderTable(id, numberOfGuests, empty);
    }
}
