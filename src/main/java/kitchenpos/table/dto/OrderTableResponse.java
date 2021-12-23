package kitchenpos.table.dto;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import kitchenpos.table.domain.OrderTable;

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

    public static OrderTableResponse of(final OrderTable orderTable) {
        Long tableGroupId = null;

        if (!Objects.isNull(orderTable.getTableGroup())) {
            tableGroupId = orderTable.getTableGroup().getId();
        }

        return new OrderTableResponse(orderTable.getId(), tableGroupId,
            orderTable.getNumberOfGuests().getNumberOfGuests(), orderTable.isEmpty());
    }

    public static List<OrderTableResponse> ofList(final List<OrderTable> orderTables) {
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
