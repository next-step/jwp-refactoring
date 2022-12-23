package kitchenpos.table.dto;


import kitchenpos.table.domain.OrderTable;
import kitchenpos.table.domain.TableGroup;


public class OrderTableResponse {
    private Long id;
    private TableGroup tableGroup;
    private int numberOfGuests;
    private boolean empty;

    public OrderTableResponse() {

    }

    public OrderTableResponse(Long id, TableGroup tableGroup, int numberOfGuests, boolean empty) {
        this.id = id;
        this.tableGroup = tableGroup;
        this.numberOfGuests = numberOfGuests;
        this.empty = empty;
    }

    public static OrderTableResponse of(OrderTable orderTable) {
        return new OrderTableResponse(orderTable.getId(), orderTable.getTableGroup(), orderTable.getNumberOfGuests(), orderTable.isEmpty());
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
}
