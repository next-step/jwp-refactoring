package kitchenpos.table.dto;

import kitchenpos.table.domain.OrderTable;

public class OrderTableRequestDto {
    private int numberOfGuests;
    private boolean empty;

    public OrderTableRequestDto() {
    }

    public OrderTableRequestDto(int numberOfGuests, boolean empty) {
        this.numberOfGuests = numberOfGuests;
        this.empty = empty;
    }

    public int getNumberOfGuests() {
        return numberOfGuests;
    }

    public boolean isEmpty() {
        return empty;
    }

    public OrderTable toEntity() {
        return new OrderTable(null, numberOfGuests, empty);
    }
}
