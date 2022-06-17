package kitchenpos.dto;

import kitchenpos.domain.OrderTableEntity;

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

    public static OrderTableResponse of(OrderTableEntity orderTable) {
        return new OrderTableResponse(orderTable.getId(), getTableGroupId(orderTable),
                                      orderTable.getNumberOfGuests().getValue(), orderTable.isEmpty());
    }

    private static Long getTableGroupId(OrderTableEntity orderTable) {
        if (orderTable.getTableGroup() == null) {
            return null;
        }
        return orderTable.getTableGroup().getId();
    }

    public static List<OrderTableResponse> of(List<OrderTableEntity> orderTables) {
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
