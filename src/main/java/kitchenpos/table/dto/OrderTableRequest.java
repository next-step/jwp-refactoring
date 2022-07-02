package kitchenpos.table.dto;

import kitchenpos.table.domain.OrderTable;
import kitchenpos.table.domain.TableGroup;

public class OrderTableRequest {
    private int numberOfGuests;
    private boolean empty;
    private TableGroup tableGroup;

    public OrderTableRequest() {
    }

    public OrderTableRequest(int numberOfGuests, boolean empty) {
        this(numberOfGuests, empty, null);
    }

    public OrderTableRequest(int numberOfGuests, boolean empty, TableGroup tableGroup) {
        this.numberOfGuests = numberOfGuests;
        this.empty = empty;
        this.tableGroup = tableGroup;
    }

    public int getNumberOfGuests() {
        return numberOfGuests;
    }

    public boolean getEmpty() {
        return empty;
    }

    public TableGroup getTableGroup() {
        return tableGroup;
    }

    public OrderTable toTableGroup() {
        return new OrderTable(numberOfGuests, empty);
    }
}
