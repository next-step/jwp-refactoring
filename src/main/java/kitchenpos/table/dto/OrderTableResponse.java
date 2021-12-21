package kitchenpos.table.dto;

import kitchenpos.table.domain.OrderTable;

import java.util.Objects;

public class OrderTableResponse {
    private Long id;
    private OrderTableGroupResponse tableGroup;
    private int numberOfGuests;
    private boolean empty;

    protected OrderTableResponse() {
    }

    public OrderTableResponse(Long id, OrderTableGroupResponse tableGroup, int numberOfGuests, boolean empty) {
        this.id = id;
        this.tableGroup = tableGroup;
        this.numberOfGuests = numberOfGuests;
        this.empty = empty;
    }

    public static OrderTableResponse of(OrderTable table) {
        OrderTableGroupResponse orderTableGroup = OrderTableGroupResponse.of(table.getTableGroup());
        return new OrderTableResponse(table.getId(), orderTableGroup, table.getNumberOfGuests(), table.isEmpty());
    }

    public Long getId() {
        return id;
    }

    public OrderTableGroupResponse getOrderTableGroup() {
        return tableGroup;
    }

    public boolean isEmpty() {
        return empty;
    }

    public int getNumberOfGuests() {
        return numberOfGuests;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        OrderTableResponse that = (OrderTableResponse) o;
        return numberOfGuests == that.numberOfGuests
                && empty == that.empty
                && Objects.equals(id, that.id)
                && Objects.equals(tableGroup, that.tableGroup);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, tableGroup, numberOfGuests, empty);
    }
}
