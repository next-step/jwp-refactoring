package kitchenpos.table.ui.response;

import java.util.List;
import java.util.stream.Collectors;
import kitchenpos.table.domain.OrderTable;

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
            return new OrderTableResponse(orderTable.id(),
                orderTable.numberOfGuests().value(),
                orderTable.isEmpty(),
                orderTable.tableGroup().id()
            );
        }
        return new OrderTableResponse(orderTable.id(),
            orderTable.numberOfGuests().value(),
            orderTable.isEmpty()
        );
    }

    public static List<OrderTableResponse> listFrom(List<OrderTable> orderTables) {
        return orderTables.stream()
            .map(OrderTableResponse::from)
            .collect(Collectors.toList());
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
