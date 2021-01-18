package kitchenpos.dto;

import kitchenpos.domain.OrderTable;

import java.util.Objects;

public class OrderTableRequest {
    private Integer numberOfGuests;
    private Boolean empty;

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

    public Integer getNumberOfGuests() {
        return numberOfGuests;
    }

    public Boolean isEmpty() {
        return empty;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        OrderTableRequest that = (OrderTableRequest) o;
        return Objects.equals(numberOfGuests, that.numberOfGuests) && Objects.equals(empty, that.empty);
    }

    @Override
    public int hashCode() {
        return Objects.hash(numberOfGuests, empty);
    }

    @Override
    public String toString() {
        return "OrderTableRequest{" +
                "numberOfGuests=" + numberOfGuests +
                ", empty=" + empty +
                '}';
    }
}
