package kitchenpos.dto.request;

import kitchenpos.orderTable.domain.OrderTable;

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

    public int getNumberOfGuests() {
        return numberOfGuests;
    }

    public boolean isEmpty() {
        return empty;
    }

    public OrderTable toEntity() {
        return new OrderTable(id, numberOfGuests);
    }
}
