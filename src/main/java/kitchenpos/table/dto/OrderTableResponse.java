package kitchenpos.table.dto;

import kitchenpos.table.domain.OrderTable;

import java.util.Objects;

public class OrderTableResponse {

    private final Long id;
    private final Long tableGroupId;
    private final int numberOfGuests;
    private final boolean empty;

    private OrderTableResponse(Long id, Long tableGroupId, int numberOfGuests, boolean empty) {
        this.id = id;
        this.tableGroupId = tableGroupId;
        this.numberOfGuests = numberOfGuests;
        this.empty = empty;
    }

    public static OrderTableResponse of(OrderTable orderTable) {
        return new OrderTableResponse(
                orderTable.getId(),
                orderTable.getTableGroupId(),
                orderTable.getNumberOfGuests(),
                orderTable.isEmpty()
        );
    }

    public Long getId() {
        return id;
    }

    public Long getTableGroupId() {
        return tableGroupId;
    }

    public int getNumberOfGuests() {
        return numberOfGuests;
    }

    public boolean isEmpty() {
        return empty;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        OrderTableResponse that = (OrderTableResponse) o;
        return getNumberOfGuests() == that.getNumberOfGuests() && isEmpty() == that.isEmpty() && Objects.equals(getTableGroupId(), that.getTableGroupId());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getTableGroupId(), getNumberOfGuests(), isEmpty());
    }
}
