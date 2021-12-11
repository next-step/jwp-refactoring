package kitchenpos.table.ui.response;

import java.util.List;
import java.util.stream.Collectors;
import kitchenpos.table.domain.OrderTable;
import kitchenpos.table.domain.OrderTables;

public final class OrderTableResponse {

    private long id;
    private Long tableGroupId;
    private int numberOfGuests;
    private boolean empty;

    private OrderTableResponse() {
    }

    private OrderTableResponse(long id, int numberOfGuests, boolean empty) {
        this(id, numberOfGuests, empty, null);
    }

    private OrderTableResponse(long id, int numberOfGuests, boolean empty, Long tableGroupId) {
        this.id = id;
        this.tableGroupId = tableGroupId;
        this.numberOfGuests = numberOfGuests;
        this.empty = empty;
    }

    public static OrderTableResponse from(OrderTable orderTable) {
        if (orderTable.hasTableGroup()) {
            new OrderTableResponse(orderTable.getId(),
                orderTable.getNumberOfGuests().value(),
                orderTable.isEmpty(),
                orderTable.getTableGroupId()
            );
        }
        return new OrderTableResponse(orderTable.getId(),
            orderTable.getNumberOfGuests().value(),
            orderTable.isEmpty()
        );
    }

    public static List<OrderTableResponse> listFrom(List<OrderTable> orderTables) {
        return orderTables.stream()
            .map(OrderTableResponse::from)
            .collect(Collectors.toList());
    }

    public static List<OrderTableResponse> listFrom(OrderTables orderTables) {
        return listFrom(orderTables.list());
    }

    public long getId() {
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
