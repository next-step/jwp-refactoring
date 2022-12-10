package kitchenpos.ordertable.dto;

import kitchenpos.ordertable.domain.OrderTable;
import kitchenpos.tablegroup.domain.TableGroup;

public class OrderTableResponse {
    private Long id;
    private Long tableGroup;
    private int numberOfGuests;
    private boolean empty;

    private OrderTableResponse(Long id, Long tableGroup, int numberOfGuests, boolean empty) {
        this.id = id;
        this.tableGroup = tableGroup;
        this.numberOfGuests = numberOfGuests;
        this.empty = empty;
    }

    public static OrderTableResponse from(OrderTable orderTable) {
        Long id = orderTable.getId();
        int numberOfGuests = orderTable.getNumberOfGuests();
        boolean empty = orderTable.isEmpty();
        TableGroup tableGroup = orderTable.getTableGroup();

        if (tableGroup == null) {
            return new OrderTableResponse(id, null, numberOfGuests, empty);
        }

        return new OrderTableResponse(id, tableGroup.getId(), numberOfGuests, empty);
    }

    public Long getId() {
        return id;
    }

    public Long getTableGroup() {
        return tableGroup;
    }

    public int getNumberOfGuests() {
        return numberOfGuests;
    }

    public boolean isEmpty() {
        return empty;
    }
}
