package table.dto;

import java.util.*;

import table.domain.*;

public class OrderTableResponse {
    private Long id;
    private TableGroup tableGroup;
    private int numberOfGuests;
    private boolean empty;

    public OrderTableResponse() {
    }

    public OrderTableResponse(OrderTable orderTable) {
        this.id = orderTable.getId();
        this.tableGroup = orderTable.getTableGroup();
        this.numberOfGuests = orderTable.getNumberOfGuests();
        this.empty = orderTable.isEmpty();
    }

    public static OrderTableResponse of(OrderTable orderTable) {
        return new OrderTableResponse(orderTable);
    }

    public Long getId() {
        return id;
    }

    public TableGroup getTableGroup() {
        return tableGroup;
    }

    public int getNumberOfGuests() {
        return numberOfGuests;
    }

    public boolean isEmpty() {
        return empty;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;
        OrderTableResponse that = (OrderTableResponse)o;
        return numberOfGuests == that.numberOfGuests && empty == that.empty && Objects.equals(tableGroup,
            that.tableGroup);
    }

    @Override
    public int hashCode() {
        return Objects.hash(tableGroup, numberOfGuests, empty);
    }
}
