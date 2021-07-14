package kitchenpos.dto.ordertable;


import kitchenpos.application.ordertable.OrderTable;
import kitchenpos.application.tablegroup.TableGroup;

import java.util.List;
import java.util.stream.Collectors;

public class OrderTableResponse {
    private Long id;

    private Long tableGroupId;

    private int numberOfGuests;

    private boolean empty;

    private OrderTableResponse(Long id, Long tableGroupId, int numberOfGuests, boolean empty) {
        this.id = id;
        this.tableGroupId = tableGroupId;
        this.numberOfGuests = numberOfGuests;
        this.empty = empty;
    }

    public static OrderTableResponse of(OrderTable orderTable) {
        TableGroup tableGroup = orderTable.getTableGroup();
        Long tableGroupId = tableGroup == null ? null : tableGroup.getId();
        return new OrderTableResponse(orderTable.getId(),
                tableGroupId,
                orderTable.getNumberOfGuests(),
                orderTable.isEmpty());
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