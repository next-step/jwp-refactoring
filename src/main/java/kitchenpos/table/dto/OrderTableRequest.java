package kitchenpos.table.dto;

import kitchenpos.table.domain.OrderTableEntity;

public class OrderTableRequest {
    private Long id;
    private int numberOfGuests;
    private boolean isEmpty;

    public OrderTableRequest() {
    }

    public OrderTableRequest(Long id, int numberOfGuests, boolean isEmpty) {
        this.id = id;
        this.numberOfGuests = numberOfGuests;
        this.isEmpty = isEmpty;
    }

    public OrderTableRequest(int numberOfGuests, boolean isEmpty) {
        this.numberOfGuests = numberOfGuests;
        this.isEmpty = isEmpty;
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

    public OrderTableEntity toEntity() {
        return new OrderTableEntity(numberOfGuests, isEmpty);
    }
}
