package kitchenpos.table.dto;

import kitchenpos.table.domain.NumberOfGuests;
import kitchenpos.table.domain.OrderTable;
import kitchenpos.table.domain.TableGroup;

public class OrderTableResponse {

    private final TableGroupResponse tableGroup;
    private final NumberOfGuests numberOfGuests;
    private final boolean empty;
    private Long id;

    public OrderTableResponse(Long id, TableGroup tableGroup, NumberOfGuests numberOfGuests, boolean empty) {
        this.id = id;
        this.numberOfGuests = numberOfGuests;
        this.empty = empty;
        if (tableGroup == null) {
            this.tableGroup = null;
            return;
        }
        this.tableGroup = TableGroupResponse.of(tableGroup);
    }

    public static OrderTableResponse of(OrderTable orderTable) {
        return new OrderTableResponse(orderTable.getId(), orderTable.getTableGroup(), orderTable.getNumberOfGuests(), orderTable.isEmpty());
    }

    public Long getId() {
        return this.id;
    }

    public TableGroupResponse getTableGroup() {
        return this.tableGroup;
    }

    public boolean isEmpty() {
        return this.empty;
    }

    public NumberOfGuests getNumberOfGuests() {
        return this.numberOfGuests;
    }
}
