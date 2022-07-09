package kitchenpos.dto.table;

import kitchenpos.domain.table.OrderTable;

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

    public static OrderTableResponse from(OrderTable orderTable) {
        return  new OrderTableResponse(orderTable.getId(), orderTable.getTableGroupId(),
                                        orderTable.getNumberOfGuests(), orderTable.isEmpty());
    }

    public static List<OrderTableResponse> ofResponses(List<OrderTable> orderTables) {
        return orderTables.stream()
                            .map(OrderTableResponse::from)
                            .collect(Collectors.toList());
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
