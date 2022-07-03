package kitchenpos.orders.table.dto;

import java.util.List;
import java.util.stream.Collectors;
import kitchenpos.orders.table.domain.OrderTable;

public class OrderTableResponse {
    private Long id;
    private Long tableGroupId;
    private int numberOfGuests;
    private boolean empty;

    protected OrderTableResponse() {
    }

    protected OrderTableResponse(Long id, Long tableGroupId, int numberOfGuests, boolean empty) {
        this.id = id;
        this.tableGroupId = tableGroupId;
        this.numberOfGuests = numberOfGuests;
        this.empty = empty;
    }

    public static OrderTableResponse from(OrderTable orderTable) {
        return new OrderTableResponse(orderTable.getId(),
                orderTable.getTableGroupId() == null ? null : orderTable.getTableGroupId(),
                orderTable.getNumberOfGuests(),
                orderTable.isEmptyTable());
    }

    public static List<OrderTableResponse> from(List<OrderTable> orderTables) {
        return orderTables.
                stream().
                map(OrderTableResponse::from).
                collect(Collectors.toList());
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
