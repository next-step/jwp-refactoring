package kitchenpos.table.dto;

import kitchenpos.table.domain.OrderTable;

public class OrderTableResponse {
    private Long id;
    private Integer numberOfGuests;
    private boolean empty;

    public OrderTableResponse(Long id, int numberOfGuests, boolean empty) {
        this.id = id;
        this.numberOfGuests = numberOfGuests;
        this.empty = empty;
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

    public static OrderTableResponse of(OrderTable orderTable) {
        return new OrderTableResponse(orderTable.getId(),orderTable.getNumberOfGuests(),orderTable.isEmpty());
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        OrderTableResponse that = (OrderTableResponse) o;

        if (empty != that.empty) return false;
        if (id != null ? !id.equals(that.id) : that.id != null) return false;
        return numberOfGuests != null ? numberOfGuests.equals(that.numberOfGuests) : that.numberOfGuests == null;
    }

    @Override
    public int hashCode() {
        int result = id != null ? id.hashCode() : 0;
        result = 31 * result + (numberOfGuests != null ? numberOfGuests.hashCode() : 0);
        result = 31 * result + (empty ? 1 : 0);
        return result;
    }
}
