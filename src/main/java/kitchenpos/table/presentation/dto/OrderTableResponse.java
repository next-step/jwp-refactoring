package kitchenpos.table.presentation.dto;

import kitchenpos.table.domain.OrderTable;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class OrderTableResponse {
    private Long id;
    private Long tableGroupId;
    private int numberOfGuests;
    private boolean empty;

    protected OrderTableResponse() {
    }

    private OrderTableResponse(Long id, Long tableGroupId, int numberOfGuests, boolean empty) {
        this.id = id;
        this.tableGroupId = tableGroupId;
        this.numberOfGuests = numberOfGuests;
        this.empty = empty;
    }

    public static OrderTableResponse of(OrderTable orderTable) {
        if (Objects.isNull(orderTable.getTableGroup())) {
            return new OrderTableResponse(orderTable.getId(), null, orderTable.getNumberOfGuests(), orderTable.isEmpty());
        }
        return new OrderTableResponse(orderTable.getId(), orderTable.getTableGroup().getId(), orderTable.getNumberOfGuests(), orderTable.isEmpty());
    }

    public static List<OrderTableResponse> ofList(List<OrderTable> orderTables) {
        return orderTables.stream()
                .map(OrderTableResponse::of)
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
