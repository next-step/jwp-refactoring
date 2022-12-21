package kitchenpos.order.dto;

import kitchenpos.order.domain.OrderTable;
import kitchenpos.tablegroup.domain.TableGroup;

import java.util.Objects;

public class TableResponse {
    private Long id;
    private Long tableGroup;
    private int numberOfGuests;
    private boolean empty;

    private TableResponse(Long id, Long tableGroup, int numberOfGuests, boolean isEmpty) {
        this.id = id;
        this.tableGroup = tableGroup;
        this.numberOfGuests = numberOfGuests;
        this.empty = isEmpty;
    }

    public static TableResponse from(OrderTable orderTable) {
        return new TableResponse(
                orderTable.getId(),
                orderTable.getTableGroupId(),
                orderTable.getNumberOfGuests(),
                orderTable.isEmpty()
        );
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
