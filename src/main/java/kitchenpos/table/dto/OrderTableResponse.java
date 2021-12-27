package kitchenpos.table.dto;

import kitchenpos.table.domain.OrderTable;
import kitchenpos.table.domain.OrderTables;

import java.util.List;
import java.util.stream.Collectors;

public class OrderTableResponse {
    private Long id;
    private Long tableGroupId;
    private int numberOfGuests;
    private boolean empty;

    public OrderTableResponse() {
    }

    public OrderTableResponse(Long id, Long tableGroupId, int numberOfGuests, boolean empty) {
        this.id = id;
        this.tableGroupId = tableGroupId;
        this.numberOfGuests = numberOfGuests;
        this.empty = empty;
    }

    public static List<OrderTableResponse> of(OrderTables orderTables) {
        return orderTables.getOrderTables()
                .stream()
                .map(orderTable -> OrderTableResponse.of(orderTable))
                .collect(Collectors.toList());
    }

    private static OrderTableResponse of(OrderTable orderTable) {
        return new OrderTableResponse(orderTable.getId(), orderTable.getTableGroupId(), orderTable.getNumberOfGuests(), orderTable.isEmpty());
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
}
