package table.dto;

import table.domain.OrderTable;

import java.util.List;
import java.util.stream.Collectors;

public class OrderTableResponse {

    private Long id;
    private Long tableGroupId;
    private int numberOfGuests;
    private boolean empty;

    protected OrderTableResponse(Long id, Long tableGroupId, int numberOfGuests, boolean empty) {
        this.id = id;
        this.tableGroupId = tableGroupId;
        this.numberOfGuests = numberOfGuests;
        this.empty = empty;
    }

    protected OrderTableResponse() {
    }

    public static OrderTableResponse of(OrderTable orderTable) {
        return new OrderTableResponse(orderTable.getId(),
                getTableGroupId(orderTable),
                orderTable.getNumberOfGuests(),
                orderTable.isEmpty());
    }

    private static Long getTableGroupId(OrderTable orderTable) {
        if(orderTable.getTableGroup() == null) {
            return null;
        }
        return orderTable.getTableGroup().getId();
    }

    public static List<OrderTableResponse> of(List<OrderTable> orderTables) {
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
