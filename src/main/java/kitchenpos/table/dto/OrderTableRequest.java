package kitchenpos.table.dto;

import kitchenpos.table.domain.OrderTableEntity;

public class OrderTableRequest {
    private int numberOfGuests;
    private boolean isEmpty;

    public OrderTableRequest() {
    }

    public OrderTableRequest(int numberOfGuests, boolean isEmpty) {
        this.numberOfGuests = numberOfGuests;
        this.isEmpty = isEmpty;
    }

    public int getNumberOfGuests() {
        return numberOfGuests;
    }

    public boolean isEmpty() {
        return isEmpty;
    }

    public OrderTableEntity toEntity() {
        return new OrderTableEntity(numberOfGuests, isEmpty);
    }
}
