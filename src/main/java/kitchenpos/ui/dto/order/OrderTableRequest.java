package kitchenpos.ui.dto.order;

import kitchenpos.domain.OrderTable;

import java.util.List;
import java.util.stream.Collectors;

public class OrderTableRequest {
    private Long id;
    private Long tableGroupId;
    private int numberOfGuests;
    private boolean empty;

    protected OrderTableRequest() {
    }

    private OrderTableRequest(Long id, Long tableGroupId, int numberOfGuests, boolean empty) {
        this.id = id;
        this.tableGroupId = tableGroupId;
        this.numberOfGuests = numberOfGuests;
        this.empty = empty;
    }

    public static OrderTableRequest of(OrderTable orderTable) {
        return new OrderTableRequest(orderTable.getId(), orderTable.getTableGroupId(), orderTable.getNumberOfGuests(), orderTable.isEmpty());
    }

    public static List<OrderTableRequest> ofList(List<OrderTable> orderTables) {
        return orderTables.stream()
                .map(OrderTableRequest::of)
                .collect(Collectors.toList());
    }

    public OrderTable toOrderTable() {
        return OrderTable.of(id,tableGroupId,numberOfGuests,empty);
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
