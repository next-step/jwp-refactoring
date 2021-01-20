package kitchenpos.dto;

import kitchenpos.domain.OrderTable;

public class OrderTableRequest {
    private Long id;
    private Integer numberOfGuests;
    private Boolean empty;

    public OrderTableRequest(Long id) {
        this.id = id;
    }

    public OrderTableRequest(Integer numberOfGuests, Boolean empty) {
        this.numberOfGuests = numberOfGuests;
        this.empty = empty;
    }

    public OrderTableRequest(Boolean empty) {
        this.empty = empty;
    }

    public OrderTableRequest(Integer numberOfGuests) {
        this.numberOfGuests = numberOfGuests;
    }

    public OrderTableRequest() {
    }

    public OrderTable toOrderTable() {
        return new OrderTable(numberOfGuests, empty);
    }

    public Long getId() {
        return id;
    }

    public Integer getNumberOfGuests() {
        return numberOfGuests;
    }

    public Boolean isEmpty() {
        return empty;
    }

    public void validateNumberOfGuests() {
        if (numberOfGuests < 0) {
            throw new IllegalArgumentException("손님 수는 0명 이상이어야합니다.");
        }
    }
}
