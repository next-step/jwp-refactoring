package kitchenpos.table.dto;

import kitchenpos.table.domain.OrderTable;

import java.util.List;
import java.util.Objects;

import static java.util.stream.Collectors.toList;

public class OrderTableResponse {
    private Long id;
    private Long tableGroupId;
    private int numberOfGuests;
    private boolean empty;

    public OrderTableResponse() {}

    public OrderTableResponse(OrderTable orderTable) {
        this.id = orderTable.getId();
        if (Objects.nonNull(orderTable.getTableGroup())) {
            this.tableGroupId = orderTable.getTableGroup().getId();
        }
        this.numberOfGuests = orderTable.getNumberOfGuests();
        this.empty = orderTable.isEmpty();
    }

    public static OrderTableResponse of(OrderTable orderTable) {
        return new OrderTableResponse(orderTable);
    }

    public static List<OrderTableResponse> list(List<OrderTable> orderTables) {
        return orderTables.stream().map(OrderTableResponse::new).collect(toList());
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
