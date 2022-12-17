package kitchenpos.ordertable.dto;

import java.time.LocalDateTime;
import java.util.List;
import kitchenpos.ordertable.domain.NumberOfGuests;
import kitchenpos.ordertable.domain.OrderTable;
import kitchenpos.tablegroup.domain.TableGroup;

public class OrderTableRequest {
    private int numberOfGuests;
    private boolean empty;

    protected OrderTableRequest() {}

    public OrderTableRequest(int numberOfGuests, boolean empty) {
        this.numberOfGuests = numberOfGuests;
        this.empty = empty;
    }

    public static OrderTableRequest of(int numberOfGuests, boolean empty) {
        return new OrderTableRequest(numberOfGuests, empty);
    }

    public int getNumberOfGuests() {
        return numberOfGuests;
    }

    public boolean isEmpty() {
        return empty;
    }

    public TableGroup createTableGroup(List<OrderTable> orderTables) {
        TableGroup tableGroup = new TableGroup(LocalDateTime.now(), orderTables);
        orderTables.forEach(table -> table.setTableGroup(tableGroup));
        return tableGroup;
    }

    public OrderTable createOrderTable() {
        OrderTable orderTable = new OrderTable(new NumberOfGuests(numberOfGuests), empty);
        orderTable.setTableGroup(null);

        return orderTable;
    }
}
