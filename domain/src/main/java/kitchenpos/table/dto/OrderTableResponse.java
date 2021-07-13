package kitchenpos.table.dto;

import kitchenpos.table.domain.OrderTable;
import kitchenpos.table.domain.TableStatus;

import java.util.List;
import java.util.stream.Collectors;

public class OrderTableResponse {
    private Long id;
    private Long tableGroupId;
    private int numberOfGuests;
    private TableStatus tableStatus;

    public OrderTableResponse() {
    }

    public OrderTableResponse(Long id, Long tableGroupId, int numberOfGuests, TableStatus tableStatus) {
        this.id = id;
        this.tableGroupId = tableGroupId;
        this.numberOfGuests = numberOfGuests;
        this.tableStatus = tableStatus;
    }

    public static OrderTableResponse of(OrderTable orderTable) {
        return new OrderTableResponse(orderTable.id(), groupId(orderTable), orderTable.numberOfGuests(), orderTable.tableStatus());
    }

    public static List<OrderTableResponse> ofList(List<OrderTable> orderTables) {
        return orderTables.stream().map(OrderTableResponse::of).collect(Collectors.toList());
    }

    private static Long groupId(OrderTable orderTable) {
        Long groupId = null;

        if (orderTable.tableGroupId() != null) {
            groupId = orderTable.tableGroupId();
        }
        return groupId;
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

    public TableStatus getTableStatus() {
        return tableStatus;
    }

}
